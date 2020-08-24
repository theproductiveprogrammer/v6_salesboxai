'use strict'
const { h } = require('@tpp/htm-x')

import './header.css'

export function show(store, on) {
  let ikon = h('.ikon')
  on.appendChild(h('img.logo', { src: "salesboxai-logo.png" }))
  on.appendChild(ikon)


  store.react('profile', profile => {
    if(!profile) ikon.c()
    else {
      ikon.c(
        h('.pic', profile.name[0]),
        h('.info', [
          h('.name', profile.name),
          h('.tenant', profile.tenant.name)
        ])
      )
    }
  })
}
