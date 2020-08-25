'use strict'

export function init() {
  return []
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'workflow/eventmeta/got': return payload;
    default: return state;
  }
}
