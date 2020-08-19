'use strict'
const { h, svg } = require('@tpp/htm-x')

const toolbar = require('./toolbar.js')
const flow = require('./flow.js')

export function init() {
  return {
    events: eventsInit(),
    toolbar: toolbar.init(),
    flow: flow.init(),
  }
}

export function reducer(state, type, payload) {
  return {
    toolbar: toolbar.reducer(state.toolbar, type, payload),
    flow: flow.reducer(state.flow, type, payload),
  }
}

export function show(store, e) {
  toolbar.show(store.fork('toolbar'), e)

  let fns = {
    currStep: () => {
      let tb = store.get('toolbar')
      let curr = tb.items[tb.selected]
      if(curr) return curr.id
    },
    events: () => store.get('events'),
  }
  flow.show(store.fork('flow'), fns, e)
}


function eventsInit() {
  let svg = require('./step/event.svg')
  return [
    {
      name: 'Event: New Lead',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'With New Lead') }
    },
    {
      name: 'Event: Email Open',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Email Opened') }
    },
    {
      name: 'Event: Link Clicked',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Link Click') }
    },
    {
      name: 'Event: Email Reply',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Email Reply') }
    },
    {
      name: 'Event: Chat',
      pic: { sz: 64, svg: svg.replace('EVENT_TEXT', 'On Chat Msg') }
    },
  ]

}
