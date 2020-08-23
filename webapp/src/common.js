'use strict'

export function btnKey(e, fn) {
  if(e.code == 'Space') return fn()
  if(e.code == 'Enter') return fn()
}

