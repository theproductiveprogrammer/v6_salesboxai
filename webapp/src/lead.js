'use strict'
const { h } = require('@tpp/htm-x')
const { get, post, error_ } = require('./common.js')

import './lead.css'

let lead_
export function setup(rootstore) {
  lead_ = () => {
    let id = rootstore.get('lead')
    if(!id) return
    return rootstore.get('leads').filter(l => l.id = id)[0]
  }
}

export function init() {}

export function reducer(state, type, payload) {
  switch(type) {
    case 'leads/lead/go': return payload
    default: return state
  }
}

export function show(store, on) {
  let lead = h('.lead')
  on.appendChild(lead)

  let l = lead_()
  let title = h('.title', `${l.firstName} ${l.lastName}`)
  let subtitle = h('.subtitle', l.email)

  let score = h('.score', "0")
  let scoretitle = h('.scoretitle', 'Score')

  let convlist = h('.convlist')
  let convs = h('.convs', [
    h('.card-title', 'Conversations'),
    convlist
  ])
  let actlist = h('.actlist')
  let acts = h('.acts', [
    h('.card-title', 'Activities'),
    actlist
  ])


  lead.c(
    h('.cont').c(title, subtitle),
    h('.score-cont').c(score, scoretitle),
    convs,
    acts,
  )
}
