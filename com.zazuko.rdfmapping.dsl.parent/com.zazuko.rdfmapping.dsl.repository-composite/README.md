# composite update-site for end-users

  https://download.zazukoians.org/rdf-mapping-dsl/updates/

## update available versions

* update `children` entries in both XML files
* update `p2.timestamp` in both XML files to current value (unix epoch in milliseconds)
  * e.g. https://www.epochconverter.com/

## trigger publication

* tag with *composite* and push to trigger the publication
  
  git tag composite master
  git push --follow-tags
