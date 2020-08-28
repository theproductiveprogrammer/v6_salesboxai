'use strict'
const { h } = require('@tpp/htm-x')
const { get, upload, error_ } = require('./common.js')

import './importer.css'

let upload_
let get_
export function setup(rootstore) {
  get_ = (url, data, cb) => get(rootstore, url, data, cb)

  upload_ = f => upload(rootstore,
    '/importleads', 'importFile', f, (err, resp) => {
      if(err) return error_(err, 'uploading')
      getImportList(rootstore)
  })
}

function getImportList(store) {
  //TODO
  return
  get_('/imports', (err, resp) => {
    if(err) return error_(err, 'get/imports')
    store.event('imports/got', resp)
  })
}

export function show(store, on) {
  let imp = h('.import')
  on.appendChild(imp)
  getImportList(store)

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

  ;['dragenter', 'dragover', 'dragleave', 'drop'].forEach(n => {
    importarea.addEventListener(n, preventDefaults, false)
  })

  function preventDefaults(e) {
    e.preventDefault()
    e.stopPropagation()
  }

  ;['dragenter', 'dragover'].forEach(n => {
    importarea.addEventListener(n,
      () => importarea.classList.add('highlight')
    )
  })

  ;['dragleave', 'drop'].forEach(n => {
    importarea.addEventListener(n,
      () => importarea.classList.remove('highlight')
    )
  })

  importarea.addEventListener('drop', handleDrop, false)

  function handleDrop(e) {
    let dt = e.dataTransfer
    let files = dt.files
    if(files[0]) upload_(files[0])
  }

  imp.c(
    title, importarea,
    h('.cont').c(tbltitle, tbl)
  )

}
