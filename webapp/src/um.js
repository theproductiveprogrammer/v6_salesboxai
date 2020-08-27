'use strict'
const { h } = require('@tpp/htm-x')
const req = require('@tpp/req')

const { btnKey, error_ } = require('./common.js')

import './um.css'

export function init() {
  return null
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'um/loggedin': return payload
    default: return state
  }
}

export function showSignup(store, on) {
  let um = h('.um')
  on.appendChild(um)

  let card = h('.card.signup')
  let title = h('h1', "Sign Up")
  let name = h('input')
  let userid = h('input')
  let password = h('input', { type: 'password' })
  let tenant = h('select')
  let btn = h('.btn.disabled', { tabindex:0 }, 'Sign Up')
  let submit = h('.row', btn)
  let toggle = h('.row',
    h('a.toggle', {
      href: '#',
      onclick: () => store.event('um/login')
    }, "Login")
  )

  um.c(
    card.c(
      title,
      h('.row', [ h('.label', "Name"), name ]),
      h('.row', [ h('.label', "Username"), userid ]),
      h('.row', [ h('.label', "Password"), password ]),
      h('.row', [ h('.label', "Tenant"), tenant ]),
      submit,
      toggle,
    )
  )

  req.get('/tenants', (err, tenants) => {
    if(err) return error_(err, 'get/tenants')
    btn.classList.remove('disabled')
    btn.onclick = signup_1
    btn.onkeydown = e => btnKey(e, signup_1)
    tenant.appendChild(h('option', {
      disabled: true,
      selected: true,
      value: 0,
    }, '-- select a tenant --'))
    for(let i = 0;i < tenants.length;i++) {
      tenant.appendChild(h('option', {
        value: tenants[i].id
      }, tenants[i].name))
    }
  })


  function signup_1() {
    let info = {
      userid: userid.value,
      password: password.value,
      tenantId: tenant.options[tenant.selectedIndex].value,
      name: name.value,
    }
    if(!info.userid || !info.password
      || !info.tenantId || info.tenantId == '0' || !info.name) {
      return error_('Please fill in all fields')
    }
    btn.classList.add('disabled')
    req.post('/signup', info, (err, resp) => {
      if(err) return error_(err, 'signup')
      if(!resp) return error_('Signup failed')
      store.event('um/login')
    })
  }

}

export function showLogin(store, on) {
  let um = h('.um')
  on.appendChild(um)

  let card = h('.card.login')
  let title = h('h1', "Login")
  let username = h('input')
  let password = h('input', { type: 'password' })
  let btn = h('.btn', {
    tabindex: 0,
    onclick: login_1,
    onkeydown: e => btnKey(e,login_1),
  }, 'Login')
  let submit = h('.row', btn)
  let toggle = h('.row',
    h('a.toggle', {
      href: '#',
      onclick: () => store.event('um/signup'),
    }, "Sign Up")
  )

  um.c(
    card.c(
      title,
      h('.row', [ h('.label', "Username"), username ]),
      h('.row', [ h('.label', "Password"), password ]),
      submit,
      toggle,
    )
  )

  function login_1() {
    let info = {
      username: username.value,
      password: password.value,
    }
    if(!info.username || !info.password) {
      return error_('Please fill in all fields')
    }
    btn.classList.add('disabled')
    req.post('login', info, (err, resp) => {
      btn.classList.remove('disabled')
      if(err) return error_(err, 'login')
      if(!resp.username || !resp.access_token) {
        return error_('Login failed')
      }
      store.event('um/loggedin', resp)
    })
  }

}
