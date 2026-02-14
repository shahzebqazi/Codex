# Base Repository Guidelines

<!-- AI: Contains subprompts. Scan for task-specific instructions. Preferences: see PREFERENCES block if present. -->
<!-- PREFERENCES (edit for your project): -->
<!-- (none) -->

This repository serves as the **base repo** -- a foundational template and shared foundation that other projects are built upon. All design decisions must uphold these two roles:

## 1. As a Project Template / Scaffold

- The repo must be **clonable and immediately runnable** as a standalone starting point for new projects.
- Provide sensible defaults, starter configurations, and boilerplate that new projects inherit out of the box (e.g., linting, formatting, CI/CD pipelines, directory structure, dependency management).
- Include a clear `README.md` with setup instructions, prerequisites, and a "Getting Started" guide so a new developer can go from clone to running in minutes.
- Use **placeholder values** (clearly marked) for project-specific details (name, description, environment variables, etc.) so they are easy to find and replace.
- Keep the dependency footprint minimal and intentional -- only include what is genuinely shared across all downstream projects.
- Tag stable, template-ready states with version tags (e.g., `template/v1.0`) so new projects can bootstrap from a known-good snapshot.

## 2. As a Shared Foundation for Dependent Repos

- The repo must define and export **shared libraries, utilities, configurations, and conventions** that downstream repos consume as a dependency (not just copy).
- Maintain strict **semantic versioning** so dependent repos can pin to compatible versions and upgrade predictably.
- All public APIs, shared modules, and exported configurations must be **well-documented and stable** -- breaking changes require a major version bump and a migration guide.
- Provide **extension points** (hooks, overrides, plugin interfaces) so downstream repos can customize behavior without forking or patching the base.
- Include a comprehensive **test suite** covering all shared functionality to guarantee reliability for all consumers.
- Changes to the base repo must be evaluated for their **downstream impact** -- consider requiring a compatibility matrix or integration tests against key dependent repos before merging.

## General Principles

- **Backward compatibility** is a first-class concern. Avoid breaking changes; deprecate before removing.
- **Separation of concerns**: clearly delineate what belongs in the base repo vs. what belongs in downstream projects. The base repo should contain only what is truly shared.
- **Documentation over convention**: when in doubt, document it. Downstream developers should never have to read source code to understand how to use or extend the base.
- Keep the repo **lean and opinionated** -- it is better to provide a strong, clear foundation than a bloated one that tries to cover every possible use case.
- Automate where possible: dependency updates, changelog generation, version bumping, and template validation should all be part of CI.
