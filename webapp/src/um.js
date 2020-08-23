'use strict'
const { h } = require('@tpp/htm-x')

import './um.css'

export function init() {
}

export function reducer(state, type, payload) {
}

export function showSignup(store, on) {
  on.appendChild(h('h1', "Sign Up"))
}

export function showLogin(store, on) {
  let um = h('.um')
  on.appendChild(um)

  let card = h('.card')
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

  um.c(
    card.c(
      title,
      username,
      password,
      submit,
    )
  )

  function login_1() {
    btn.classList.add('disabled')
  }

}
