'use strict'
const { h, svg } = require('@tpp/htm-x')

import './toolbar.css'

export function init() {
  return { items: [], selected: -1 }
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'workflow/stepmeta/got': return {
      items: payload.map(m => {
        return { name:m.name, code: m.code, icon:m.icon }
      }),
      selected: 0
    }
    case 'workflow/toolbar/selected': return {
      ...state,
      selected: payload
    }
    default: return state
  }
}

export function show(store, e) {
  let tb = h('.toolbar')
  let icons = []
  e.appendChild(tb)

  store.react('items', items => {
    tb.setAttribute("width", 50 * items.length + 50)
    let i = 0
    for(;i < items.length;i++) {
      if(icons[i]) continue
      icons[i] = toolbarIcon(store, i)
      tb.appendChild(icons[i].e)
    }
    while(i < icons.length) {
      let icon = icons.pop()
      store.unreact(icon.fn)
      tb.removeChild(icon.e)
    }
  })

  store.react('selected', sel => {
    for(let i = 0;i < icons.length;i++) {
      if(sel == i) icons[i].e.classList.add('selected')
      else icons[i].e.classList.remove('selected')
    }
  })

}

function toolbarIcon(store, i) {
  let sz = 40
  let e = h('.icon', {
    onclick: () => store.event('workflow/toolbar/selected', i)
  })

  let fn = store.react(`items.${i}`, tool => {
    e.setAttribute('title', tool.name)
    e.c(
      h('img', { width: sz, height: sz, src: tool.icon })
    )
  })

  return { e, fn }
}

