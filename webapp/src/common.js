'use strict'
const req = require('@tpp/req')

export function svgPos(svg_, pt, e) {
  pt.x = e.clientX
  pt.y = e.clientY
  return pt.matrixTransform(svg_.getScreenCTM().inverse())
}

export function opt(v,d) { return (v || v === 0) ? v : d }

export function error_(e) {
  console.log(e)
  if(typeof e == 'object') {
    if(e.message) e = e.message
  } else {
    e = e.toString()
  }
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
