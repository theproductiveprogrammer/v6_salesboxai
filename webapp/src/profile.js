'use strict'
const { get, error_ } = require('./common.js')

export function setup(rootstore) {
  rootstore.react('user', user => {
    if(user == null) return rootstore.event('profile/set')
    get(rootstore, '/profile', (err, resp) => {
      if(err) return error_(err, 'get/profile')
      rootstore.event('profile/set', resp)
    })
  })
}

export function init() {
  return null
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'profile/set': return payload;
    default: return state
  }
}
