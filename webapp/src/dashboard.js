'use strict'
const { h } = require('@tpp/htm-x')

import './dashboard.css'

export function show(store, on) {
  let dashboard = h('.dashboard')
  on.appendChild(dashboard)

  let leadcard = h('.card')
  let workflowcard = h('.card')
  let importcard = h('.card')

  leadcard.c(
    h('.title', "Leads"),
    h('.num', "14324"),
    h('.row', [
      h('.col', [
        h('.label', "New"),
        h('.new', "14312"),
      ]),
      h('.col', [
        h('.label', "Hot"),
        h('.hot', "12"),
      ])
    ]),
    h('img.edit', {
      src: './edit.svg',
      onclick: () => store.event('dash/leads/go')
    })

  )

  workflowcard.c(
    h('.title', "Workflows"),
    h('img', { src: './workflow.svg'}),
    h('img.edit', {
      src: './edit.svg',
      onclick: () => store.event('dash/workflow/go')
    })
  )

  importcard.c(
    h('.title', "Events"),
    h('.line', [
      h('.head', "Imports"),
      h('.val', "12"),
    ]),
    h('.line', [
      h('.head', "Clicks"),
      h('.val', "8"),
    ]),
    h('.line', [
      h('.head', "Email Opens"),
      h('.val', "2"),
    ]),
    h('.line', [
      h('.head', "Chats"),
      h('.val', "77"),
    ]),
    h('img.edit', {
      src: './edit.svg',
      onclick: () => store.event('dash/importer/go')
    })
  )

  dashboard.c(
    h('h1', "Home"),
    leadcard,
    workflowcard,
    importcard
  )
}
