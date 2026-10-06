# MySQL academic schema

Objective: Create and populate the three model-backed tables in the running MySQL `admin_bases_datos` database.

Problem/why: The MySQL database is empty after reset, while the app's Makefile points to a removed `segunda_unidad` database.

Scope: MySQL only. Preserve all existing user edits and data. Match `StudentRepository` column names, including its existing `epecialidad` spelling. Enforce `2304XXXX` controls, grade relationships, and foreign keys. No other database engines or model refactor.

Authorized: Local SQL/schema/data writes and the Makefile URL correction. Acceptance: three tables exist; sample students have valid unique control numbers; grade rows join to existing students and subjects; invalid controls and orphan grades are rejected; `make run` uses this database.

TDD: Not enabled/unknown; no configured test runner. Functional checks: MySQL DDL inspection, constraint-negative tests in rolled-back transactions, row/join counts, and `make run` if available.

Delivery: ask-on-risk; forecast ~110 authored lines. Route: delegated mapping (4+ files required), inline SQL implementation plus mechanical Makefile correction. Current branch: `feat/student-query-demo`; existing dirty changes must remain untouched except the one URL line.

- [x] T1 Create idempotent MySQL schema and deterministic related sample data; correct Makefile URL; verify constraints and application integration; commit only newly owned changes if separable.

Progress: Applied SQL twice. Counts remained 6 students, 6 subjects, and 18 grades. Invalid control and orphan grade inserts were rejected; invalid-control and orphan counts were 0. `nix-shell --run 'make run'` built successfully and displayed joined rows. `git diff --check` passed. An initial validation query using `REGEXP BINARY` was invalid on MySQL 9.7; reran with `REGEXP_LIKE`. The first SQL application hit an ambiguous `id` in an upsert, corrected before successful rerun. Makefile URL correction remains in the pre-existing dirty file and cannot be committed separately without carrying user changes.

Next: Commit owned SQL and task document only; preserve all pre-existing user changes and the Makefile URL correction in the worktree.
