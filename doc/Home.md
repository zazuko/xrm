Welcome to the rdf-mapping-dsl wiki!

# Architecture
Analysis based on [Meta-Architektur: Das richtige Setup für Ihr MDD-Projekt](https://drive.google.com/open?id=0BzHub7nw28dAZDRKVHpjQUJDLU0), please see the categories of a MDD project.

## Current Setup
The current setup of the RdfTooling is based on doing a PoC: Get a DSL up and running, and provide a showcase how this approach supports you in working with RDF. The generator chains are directly based on the ecore model generated from Xtext. This allows to bootstrap a DSL project quickly and show some results. The downside is the tight coupling of the generators to the grammar model. In order to build and improve features in the editor, the grammar model must be changeable at any time within the constraints given by the technology Xtext. Doing so obscures semantics of the model and forces to change the generator chains too (--> unneeded risk). The constraints of Xtexts also prevents to leverage the full power of EMF, since the limitations on how to use EMF models (e.g. the concrete syntax always dictates the containment hierarchy of the grammar model).

Visible symptoms of shared EMF model between generator chains and DSL editor:
* [ModelAccess.xtend](https://github.com/zazuko/rdf-mapping-dsl/blob/38eeb352bec435b789448d8a185c2b297b0036d5/com.zazuko.rdfmapping.dsl.parent/com.zazuko.rdfmapping.dsl/src/com/zazuko/rdfmapping/dsl/generator/common/ModelAccess.xtend)
  * navigation methods
  * calls to EMF.eContainer()
  * semantical shadowing, methods like *resolved,  e.g. valueResolved(RdfClass it)

After adding another fan in in #72, things get more complicated. No there are incoming references to the objects of type RdfClass and RdfProperty, which instances are controlled by different editors (xrm vs. nq). With the current achitecture, this also impacts the generator chains with clumsy constructs, e.g. [VocabularyRef](https://github.com/zazuko/rdf-mapping-dsl/blob/f2ebb56d4c633797383d3d5760012cba24584246/com.zazuko.rdfmapping.dsl.parent/com.zazuko.rdfmapping.dsl/src/com/zazuko/rdfmapping/dsl/generator/common/VocabularyRef.java). VocabularyRef abstracts the different fan-in. For a new fan-in, the fan-out has to be changed (leads to cartesian product of fan-in vs fan-out complexity).

The current setup of this MDD project is of "category A" (low sustainability, low fan-in). This architecture does not properly reflect the problems we deal with - the setup is under-engineered.

## Recommended Setup
For adressing more complex fan-in and providing higher sustainability, applying the Domain Model pattern is recommended:
![image](https://user-images.githubusercontent.com/2227963/74527113-33d1e780-4f25-11ea-852a-63b5f6b6e3dc.png)

Introducing a DomainModel means:
* normalized model
  * focus on semantics (no/less eContainer() / instanceof)
  * simplify navigation (less ModelAccess)
* protected the generator chains
  * non-semantical changes in the editor are invisible in the editor (decoupling)
* high flexibility when building aneditor
  * add new fan-in: e.g. introduce nq-files ( #72 )
  * allows to split grammar for e.g. csv and rmlish mappings (higher tweakability, no more keyword hiding)
  * constraints minimized to technical necessities


What to do in order to introduce a DomainModel
1. define domainmodel (xcore)
2. convert from dsl-model to domain-model (identity, create, fill, navigate)
3. refactor generators (de-clutter)
4. move semantical validation to domainmodel, trace diagnosis back to grammar-model (validation pojo-API, runtime as OSGi-service)

