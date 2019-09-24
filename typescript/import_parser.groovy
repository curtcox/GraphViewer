#!/usr/bin/env groovy
def trim(s,fix) {
  if (s.startsWith(fix)) s = s.substring(fix.length())
  if (s.endsWith(fix))   s = s.substring(0,s.length()-fix.length())
  s
}

def without(s,String... fixes) {
  for (f in fixes) {
    s = trim(s,f)
  }
  s
}

def importing(s) {
  without(s,'./',':','.ts')
}

def imported(s) {
  def parts = s.split('from')
  if (parts.length > 1) s = parts[1]
  without(s.trim(),';','.',"'",'"')
}

def ignore(x,list) {
  list.findAll { it != x }
}

def resolve(x,to,from) {
  def levelsUp = to.findAll { it == x } .size
  if (levelsUp == 0) return to
  from = Arrays.asList(from)
  def end = from.size() - (levelsUp + 1)
  def prefix = from.subList(0,end)
  to = ignore('..',to)
  def list = new ArrayList()
  list.addAll(prefix)
  list.addAll(to)
  list
}

def absoluteWithRespectTo(to,from) {
  def fromParts = from.split('/')
  def   toParts = to.split('/')
  toParts = ignore('.',toParts)
  toParts = resolve('..',toParts,fromParts)
  toParts.join('/')
}

System.in.withReader { r ->
    r.eachLine { line ->
        def parts = line.split('import')
        def  from = importing(parts[0])
        def    to = imported(parts[1])
        to = absoluteWithRespectTo(to,from)
        println "$from|$to"
    }
}
