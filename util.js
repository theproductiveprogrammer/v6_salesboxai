'use strict'

export function svgPos(svg_, pt, e) {
  pt.x = e.clientX
  pt.y = e.clientY
  return pt.matrixTransform(svg_.getScreenCTM().inverse())
}

export function opt(v,d) { (v || v === 0) ? v : d }
