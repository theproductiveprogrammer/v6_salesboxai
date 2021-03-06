'use strict'
const { h } = require('@tpp/htm-x')
const { get, post, error_ } = require('./common.js')

import './lead.css'

let get_
let post_
let lead_
export function setup(rootstore) {
  get_ = (url, data, cb) => get(rootstore, url, data, cb)
  post_ = (url, data, cb) => post(rootstore, url, data, cb)

  lead_ = () => {
    let id = rootstore.get('lead')
    if(!id) return
    return rootstore.get('leads').filter(l => l.id == id)[0]
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

  let eventtitle = h('.eventtitle', 'Lead Actions:')
  let evt = (code,name) => h('.event', {
    onclick: () => send_event_1(code)
  }, name)
  let emailopen = evt('email.open', 'Opened Email')
  let linkclick = evt('link.click', 'Clicked Link')
  let reply = evt('email.reply', 'Replied to Email')
  let chat = evt('chat.reply', 'Replied to Chat')

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

  let refresh = h('.refresh', { onclick: load_info_1}, 'Reload')
  let back = h('.back', {
    onclick: () => store.event('lead/goback')
  }, 'Back')

  load_info_1()

  lead.c(
    h('.nav-cont').c(back, refresh),
    h('.cont').c(
      title, subtitle,
      h('.event-cont').c(
        eventtitle, emailopen, linkclick, reply, chat
      ),
    ),
    h('.score-cont').c(score, scoretitle),
    convs,
    acts,
  )


  function send_event_1(code) {
    post_('newevent', {
      type: code,
      id: l.id,
    }, err => {
      if(err) return error_(err, 'raise/event/' + code)
    })
  }

  function load_info_1() {
    get_('conversations?leadId='+l.id, (err, conversations) => {
      if(err) return error_(err, 'get/conversations')
      convlist.c()
      conversations.forEach(c => {
        convlist.appendChild(h('.conversation', c.message))
      })
    })

    get_('activities?leadId='+l.id, (err, activities) => {
      if(err) return error_(err, 'get/activities')
      actlist.c()
      activities.forEach(c => {
        actlist.appendChild(h('.activity', c.description))
      })
    })

    get_('score?leadId='+l.id, (err, score_) => {
      if(err) return error_(err, 'get/score')
      if(score_.response) score_ = score_.response
      score.c(""+score_)
    })
  }


}
