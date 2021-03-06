'use strict'
const { h, svg } = require('@tpp/htm-x')

const { opt, svgPos, post, get, error_ }=require('../common.js')

import './flow.css'

let post_
let get_
export function setup(store) {
  post_ = (url, data, cb) => post(store, url, data, cb)
  get_ = (url, data, cb) => get(store, url, data, cb)

  store.react('user', user => {
    if(user == null) {
      store.event('workflow/flows/got', {})
      return
    }
    get_('/workflows', (err, resp) => {
      if(err) return error_(err, 'get/workflows')
      else store.event('workflow/flows/got', load(resp))
    })
  })
}

export function init() {
  return {
    meta: [],
    events: [],
    onevent: null,
    steps: [],
    selected: -1,
    flows: {}
  }
}

export function reducer(state, type, payload) {
  switch(type) {

    case 'workflow/stepmeta/got': return {
      ...state,
      meta: payload,
    }

    case 'workflow/eventmeta/got': return {
      ...state,
      onevent: payload[0],
      events: payload,
    }

    case 'workflow/flow/clear': {
      state = { ...state }
      if(state.onevent) {
        let flows = state.flows
        state.flows = {}
        for(let k in flows) {
          if(k == state.onevent.code) continue
          state.flows[k] = flows[k]
        }
      }
      state.steps = []
      state.selected = -1
      return state
    }

    case 'workflow/event/sel': return selEvent(state, payload)

    case 'workflow/flow/switch': return switchEvent(state)

    case 'workflow/flows/got': return {
      ...state,
      flows: payload,
    }

    case 'workflow/step/new':
      return newStep(state, type, payload)

    case 'workflow/step/selected': return {
      ...state,
      selected: payload
    }

    default: return state
  }
}

function switchEvent(state) {
  state = { ...state  }
  if(state.onevent && state.flows[state.onevent.code]) {
    let curr = state.flows[state.onevent.code]
    state.steps = save2steps(state.meta, state.events, curr.steps)
    state.selected = curr.selected
  } else {
    state.steps = []
    state.selected = -1
  }
  return state
}

function selEvent(state, payload) {
  state = { ...state }
  if(state.onevent) {
    state.flows[state.onevent.code] = {
      steps: steps2save(state.steps),
      selected: state.selected,
    }
  }
  state.onevent = state.events.filter(e => e.id == payload)[0]
  return state
}

function newStep(state, type, payload) {
  state = { ...state  }
  let curr = state.steps[state.selected]
  let ndx = state.selected
  if(!curr) {
    if(!state.steps.length) {
      curr = { info: state.onevent, pos: { x: 10, y: 250 } }
      state.steps = state.steps.concat(curr)
      ndx = 0
    } else {
      curr = state.steps[state.steps.length-1]
      ndx = state.steps.length-1
    }
  }
  let n = opt(curr.info.numlinks, 1)
  if(curr.links && curr.links.length >= n) {
    console.log('will not add another link to ' + curr.name)
  } else {
    let links = opt(curr.links,[])
    curr = { ...curr, links: links.concat(state.steps.length) }
  }
  state.selected = state.steps.length
  state.steps = state.steps.concat(payload)
  state.steps[ndx] = curr
  if(state.onevent) {
    state.flows[state.onevent.code] = {
      steps: steps2save(state.steps),
      selected: state.selected,
    }
  }
  return state
}

export function show(store, e) {
  let canvas = svg({
    width: "100vw",
    height: "100vh",
    viewBox: "0 0 1100 700",
    preserveAspectRatio: "xMinYMin meet",
    ondblclick: add_icon_1,
  })
  let pt = canvas.createSVGPoint()
  let filter = svg('filter#sel', svg('feDropShadow', {
    dx: 0, dy: 0,
    stdDeviation: 1,
    "flood-color": "#3333ff",
  }))
  canvas.c(filter)
  e.appendChild(canvas)

  let sel = h('select.evt', {
    onchange: () => store.event('workflow/event/sel', sel.value)
  })
  store.react('flow.events', events => {
    sel.c()
    events.forEach(e => sel.appendChild(h('option', {
      value: e.id,
    }, e.name)))
  })

  let sav = h('.btn',{ onclick: () => save(store) }, "Save")
  let clear = h('.btn',{
    onclick: () => store.event('workflow/flow/clear')
  }, "Clear")

  let bottomtoolbar = h('.btb').c(sel, sav, clear)
  e.appendChild(bottomtoolbar)

  function add_icon_1(e) {
    let curr = curr_step_1()
    if(!curr) return
    let info = store.get('flow.meta').filter(m => m.code==curr)
    if(info && info.length) {
      info = info[0]
      let pos = svgPos(canvas, pt, e)
      if(info.iconszhint) {
        pos.x -= info.iconszhint/2
        pos.y -= info.iconszhint/2
      }
      store.event("workflow/step/new", { info, pos })
    } else {
      console.error(`Failed finding info for step: ${curr}`)
    }
  }

  function curr_step_1() {
    let tb = store.get('toolbar')
    let curr = tb.items[tb.selected]
    if(curr) return curr.code
  }

  let ex = []

  store.react('flow.steps', steps => {
    for(let i = ex.length;i < steps.length;i++) {
      let inf = dispStep(canvas, pt, store, i)
      ex.push(inf)
      canvas.appendChild(inf.e)
    }
  })

  let exLines = []
  store.react('flow.steps', steps => {
    if(!steps || !steps.length) clear_1()
    for(let i = 0;i < steps.length;i++) {
      let step = steps[i]
      let links = opt(steps[i].links, [])
      let lines = opt(exLines[i],[])
      if(links.length != lines.length) {
        for(let i = 0;i < lines.length;i++) {
          canvas.removeChild(lines[i])
        }
        lines = []
        exLines[i] = lines
        for(let i = 0;i < links.length;i++) {
          let l = line_1(step, steps[links[i]])
          if(!l) continue
          lines.push(l)
          canvas.insertBefore(l, canvas.firstChild)
        }
      }
    }

    function line_1(from, to) {
      if(!from || !to) return

      let foff = opt(from.info.iconszhint,0) / 2
      let toff = opt(to.info.iconszhint,0) / 2

      return svg('line', {
        style: {
          stroke: "#000",
          'stroke-width': 2,
        },
        x1: from.pos.x + foff,
        y1: from.pos.y + foff,
        x2: to.pos.x + toff,
        y2: to.pos.y + toff,
      })
    }
  })

  store.react('flow.selected', sel => {
    for(let i = 0;i < ex.length;i++) {
      ex[i].e.classList.remove('selected')
    }
    if(ex[sel]) ex[sel].e.classList.add('selected')
  })

  store.react('flow.onevent', onevent => {
    clear_1()
    store.event('workflow/flow/switch')
  })

  function clear_1() {
    while(ex.length) {
      let inf = ex.pop()
      canvas.removeChild(inf.e)
      store.unreact(inf.fn)
    }
    while(exLines.length) {
      let lines = exLines.pop()
      while(lines && lines.length) canvas.removeChild(lines.pop())
    }
  }

}

function dispStep(canvas, pt, store, i) {
  let sz = 96
  let e = svg('svg.step', {
    width: sz, height: sz,
    onclick: e => store.event('workflow/step/selected', i)
  })

  let fn = store.react(`flow.steps.${i}`, step => {
    if(step.info.iconszhint) {
      e.setAttribute('width', step.info.iconszhint)
      e.setAttribute('height', step.info.iconszhint)
    }
    e.setAttribute('x', step.pos.x)
    e.setAttribute('y', step.pos.y)
    e.c(svg(step.info.pic.svg))
  })

  return { fn, e }

}

function load(resp) {
  let flows = {}
  for(let i = 0;i < resp.length;i++) {
    let step = resp[i]
    if(!flows[step.eventCode]) {
      flows[step.eventCode] = {
        steps: [],
        selected: -1,
      }
    }
    let s = { code: step.code, pos: step.pos, links: step.links }
    flows[step.eventCode].steps.push(s)
  }
  return flows
}

function save(store) {
  let onevent = store.get('flow.onevent')
  let flows = store.get('flow.flows')
  let data = []
  for(let k in flows) {
    let steps = flows[k].steps
    let num = 0
    steps.forEach(step => {
      data.push({
        num,
        eventCode: k,
        code: step.code,
        links: step.links,
        pos: { x: step.pos.x, y: step.pos.y },
      })
      num++
    })
  }
  if(onevent) {
    data = data.filter(d => d.eventCode != onevent.code)
    let currsteps = store.get('flow.steps')
    let num = 0
    currsteps.forEach(step => {
      data.push({
        num,
        eventCode: onevent.code,
        code: step.info.code,
        links: step.links,
        pos: { x: step.pos.x, y: step.pos.y }
      })
      num++
    })
  }
  post_('/newsteps', data, (err, resp) => {
    if(err) return error_(err, 'saving')
    else alert('Saved successfully')
  })
}

function steps2save(steps) {
  steps = steps.map(step => {
    let r = {
      code: step.info.code,
      pos: { x: step.pos.x, y: step.pos.y }
    }
    if(step.links) r.links = step.links
    return r
  })
  return steps
}

function save2steps(meta, events, steps) {
  return steps.map(step => {
    let info = meta.filter(m => m.code == step.code)[0]
    if(step.code.startsWith('evt/')) {
       info = events.filter(e => e.code == step.code)[0]
    }
    return {
      info,
      links: step.links,
      pos: step.pos,
    }
  })
}
