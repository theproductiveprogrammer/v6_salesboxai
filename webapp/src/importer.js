'use strict'
const { h } = require('@tpp/htm-x')

import './importer.css'

export function setup(rootstore) {
}

export function show(store, on) {
  let imp = h('.import')
  on.appendChild(imp)

  let title = h('.title', 'Import')
  let importarea = h('.importarea', 'Drop lead csv here to import')
  let tblcont = h('.cont')
  let tbltitle = h('.subtitle', 'Previous Imports')
  let tblbody = h('tbody')
  let tbl = h('table', [
    h('thead',
      h('tr', [
        h('th', 'Num'),
        h('th', 'Date'),
        h('th', 'Import File'),
        h('th', 'Number of Leads'),
      ])
    ),
    tblbody,
  ])

  imp.c(
    title, importarea,
    h('.cont').c(tbltitle, tbl)
  )

}
