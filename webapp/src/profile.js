'use strict'
const { get, error_ } = require('./common.js')

export function init() {
  return null
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'profile/set': return payload;
    default: return state
  }
}

export function linkUp(store) {
  store.react('user', user => {
    if(user == null) return store.event('profile/set')
    get(store, '/profile', (err, resp) => {
      if(err) return error_(err)
      store.event('profile/set', resp)
    })
  })
}
