# Student query demo

Objective: Populate the existing `segunda_unidad` database with sample students (control `2304xxxx`), subjects, and grades, then print DAO SELECT results for AND, OR, and NOT from `main`.

Problem/why: The app currently inserts two fixed students on each run and prints no query results.

Scope/constraints: Reuse the running MySQL container and existing schema, including `alumnos.name/control/epecialidad`; preserve existing rows; avoid duplicate sample records on rerun; no schema changes or new dependencies. Authorized: local source and sample database writes. TDD: not enabled/unknown (no project setting); runner: `nix-shell --run "make run"`.

Delivery: ask-on-risk; forecast ~180 authored changed lines; route: delegated direct (mapping needed 4+ files, writing App and StudentRepository).

- [x] T1 Seed sample students, subjects, and grades idempotently; show AND, OR, NOT DAO queries in `main`. Verify build and two runs without count growth; record commit if safe.

Acceptance: console prints populated examples and results from each operator, all control numbers newly inserted match `2304xxxx`, foreign-key grades point at seeded rows, repeated run does not duplicate seed data.

Progress: T1 behavior verified. `nix-shell --run "make run"` passed three times; console displayed 3 AND, 12 OR, and 12 NOT rows. Repeated runs left demo controls/grades unchanged. `git diff --check` passed. Commit pending/skipped: App had pre-existing unstaged edits and StudentRepository was already untracked before T1; committing their full contents would include user changes. Next: review and commit only with user-owned changes properly separated.
