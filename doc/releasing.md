# Publishing a new version

Update version and run local build, inside the *com.zazuko.rdfmapping.dsl.parent* module:

    mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=0.11.0-SNAPSHOT
    mvn clean verify

Trigger gitlab to publish a release on https://download.zazukoians.org/rdf-mapping-dsl/releases/:

    git tag 0.11.0 master
    git push; git push origin 0.11.0
    
Hints for deleting tags (just in case ..):
    
    git tag -d 1.0.0
    git tag -l
    git push --delete origin 1.0.0

For end-user composite updatesite, see [repository-composite](../com.zazuko.rdfmapping.dsl.parent/com.zazuko.rdfmapping.dsl.repository-composite/)
