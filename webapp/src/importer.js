'use strict'
const { h } = require('@tpp/htm-x')
const { get, upload, error_ } = require('./common.js')

import './importer.css'

let upload_
let get_
export function setup(rootstore) {
  get_ = (url, data, cb) => get(rootstore, url, data, cb)

  upload_ = (f,cb) => upload(rootstore, '/importleads', 'importFile', f, cb)
}

function getImportList(store) {
  get_('/imports', (err, resp) => {
    if(err) return error_(err, 'get/imports')
    store.event('imports/got', resp)
  })
}

export function init() {
}

export function reducer(state, type, payload) {
  switch(type) {
    case 'imports/got': return payload
    default: return state
  }
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

  store.react(imports => {
    tblbody.innerHTML = ''
    if(!imports) return
    imports.forEach(im => {
      tblbody.appendChild(h('tr', [
        h('td', im.id),
        h('td', (new Date(im.started)).toString()),
        h('td', im.importFile),
        h('td', im.count)
      ]))
    })
  })

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
    if(files[0]) {
      let txt = importarea.innerText
      importarea.innerText = 'IMPORTING...'
      importarea.classList.add('importing')
      upload_(files[0], (err, resp) => {
        importarea.innerText = txt
        importarea.classList.remove('importing')
        if(err) return error_(err, 'uploading')
        else alert('import uploaded to server and started')
        getImportList(store)
      })
    }
  }

  imp.c(
    title, importarea,
    h('.cont').c(tbltitle, tbl)
  )

}
