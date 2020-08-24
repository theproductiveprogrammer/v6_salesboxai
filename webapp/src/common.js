'use strict'
const req = require('@tpp/req')

export function error_(e) {
  console.log(e)
  if(e.toString() == '[object Object]') e = JSON.stringify(e)
  else e = e.toString()
  alert(e)
}

export function btnKey(e, fn) {
  if(e.code == 'Space') return fn()
  if(e.code == 'Enter') return fn()
}


export function get(store, url, data, cb) {
  send_('GET', store, url, data, cb)
}

export function post(store, url, data, cb) {
  send_('POST', store, url, data, cb)
}

function send_(method, store, url, data, cb) {
  if(typeof data == 'function') {
    cb = data
    data = undefined
  }
  let headers
  let user = store.get('user')
  if(user.access_token) headers = {
    Authorization: `bearer ${user.access_token}`
  }
  req.send({
    method,
    url,
    data,
    headers,
  }, cb)
}
