'use strict'
const { h } = require('@tpp/htm-x')
const req = require('@tpp/req')

import './auth.css'

export function init() {
  return {
    tenants: [],
    user: null,
  }
}

export function reducer(state, type, payload) {
}

export function showSignup(store, on) {
  let auth = h('.auth')
  on.appendChild(auth)

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
  let btn = h('.btn', { onclick: signup_1 }, 'Sign Up')
  let submit = h('.row', btn)
  let toggle = h('.row',
    h('a.toggle', {
      href: '#',
      onclick: () => store.event('auth/login')
    }, "Login")
  )

  auth.c(
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

  function signup_1() {
    btn.classList.add('disabled')
  }

}

export function showLogin(store, on) {
  let auth = h('.auth')
  on.appendChild(auth)

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
      onclick: () => store.event('auth/signup')
    }, "Sign Up")
  )

  auth.c(
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
