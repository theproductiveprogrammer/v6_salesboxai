'use strict'
const { h } = require('@tpp/htm-x')
const req = require('@tpp/req')

import './um.css'

export function init() {
  return null
}

export function reducer(state, type, payload) {
}

export function showSignup(store, on) {
  let um = h('.um')
  on.appendChild(um)

  let card = h('.card.signup')
  let title = h('h1', "Sign Up")
  let name = h('.row', [
    h('.label', "Name"), h('input')
  ])
  let username = h('.row', [
    h('.label', "Username"), h('input')
  ])
  let password = h('.row', [
    h('.label', "Password"),
    h('input', { type: 'password' })
  ])
  let tenant = h('.row', [
    h('.label', "Tenant"),
    h('select')
  ])
  let btn = h('.btn.disabled', 'Sign Up')
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
      name,
      username,
      password,
      tenant,
      submit,
      toggle,
    )
  )

  req.get('/tenants', (err, tenants) => {
    if(err) return console.error(err)
  })


  function signup_1() {
    btn.classList.add('disabled')
  }

}

export function showLogin(store, on) {
  let um = h('.um')
  on.appendChild(um)

  let card = h('.card.login')
  let title = h('h1', "Login")
  let username = h('.row', [
    h('.label', "Username"), h('input')
  ])
  let password = h('.row', [
    h('.label', "Password"),
    h('input', { type: 'password' })
  ])
  let btn = h('.btn', { onclick: login_1 }, 'Login')
  let submit = h('.row', btn)
  let toggle = h('.row',
    h('a.toggle', {
      href: '#',
      onclick: () => store.event('um/signup')
    }, "Sign Up")
  )

  um.c(
    card.c(
      title,
      username,
      password,
      submit,
      toggle,
    )
  )

  function login_1() {
    btn.classList.add('disabled')
  }

}
