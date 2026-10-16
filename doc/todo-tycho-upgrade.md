# TODO: Upgrade Tycho

Status: open, to be done after the `cli` branch is merged (decided 2026-10-16).

## Why

The build uses Tycho 2.7.5 (`com.zazuko.rdfmapping.dsl.parent/pom.xml`), which causes several workarounds:

- **Maven version pin.** Tycho 2.7 fails with the Maven version preinstalled on GitHub runners
  (`Invalid Collect Request: null` while resolving `com.zazuko.rdfmapping.dsl`). CI, the
  Dockerfile and AGENTS.md therefore pin `maven:3.9.16-eclipse-temurin-17`.
- **Excluded unit tests.** Tycho 2.7 ships JUnit 5 providers only up to 5.8, but the target
  platform (Orbit 2023-12) brings `junit-jupiter-params` 5.10. Parameterized tests fail with
  `AbstractMethodError ... ExtensionContext.getConfigurationParameter(String, Function)`.
  Excluded in `com.zazuko.rdfmapping.dsl.tests/pom.xml`:
  - `TemplateFormatAnalyzerTest`
  - `InputOutputCompatibilitySerializationTest`
- **Provider hint.** The same pom sets `<providerHint>junit5</providerHint>`, because junit 4
  on the classpath (via `org.eclipse.xtext.testing`) made Tycho pick the junit 4 provider and
  silently run 0 tests. Recheck whether newer Tycho still needs it.

## What

- Bump `tycho-version` to a current 4.x (needs Java 17+ and Maven 3.9+).
- Fix whatever breaks in target platform resolution, packaging and p2 publishing.
- Remove the two test excludes, check all 60 tests run (58 run today).
- Try removing the Maven pin (CI `container:`, Dockerfile, AGENTS.md) or at least move it to a
  current Maven.
- Possibly also update the target platform (Eclipse release, Xtext, Orbit) while at it, or do
  that as a separate step.

## Risks / manual checks

Tycho also builds the Eclipse plugin and the p2 update site (`com.zazuko.rdfmapping.dsl.repository`,
composite site, release to download.zazukoians.org, see `releasing.md`). CI only shows that the
build passes. Before merging, someone has to check by hand:

- the update site from `com.zazuko.rdfmapping.dsl.repository/target/repository` installs
  into a current Eclipse (see `dev-build.md`) and the editor works
- the language server jar (`*-ls.jar`) still works in the VS Code extension

## Safety net

CI (`.github/workflows/ci.yml`) runs the unit tests and `scripts/check-samples.sh`, which
compares the generator output of all `runtime-EclipseXtext/` samples byte for byte with the
committed `src-gen/`. Both must stay green.
