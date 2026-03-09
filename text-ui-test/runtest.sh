#!/usr/bin/env bash

# change to script directory
cd "${0%/*}"

# ── helpers ────────────────────────────────────────────────────────────────────
pass() { echo "[PASS] $1"; }
fail() { echo "[FAIL] $1"; exit 1; }

# ── step 1: build ──────────────────────────────────────────────────────────────
cd ..
./gradlew clean shadowJar
if [ $? -ne 0 ]; then
    fail "Build failed — ./gradlew clean shadowJar did not complete successfully"
fi
pass "Build"

# ── step 2: run app ────────────────────────────────────────────────────────────
cd text-ui-test
JAR=$(find ../build/libs/ -mindepth 1 -print -quit)

if [ -z "$JAR" ]; then
    fail "No jar found in ../build/libs/"
fi

java -jar "$JAR" < input.txt > ACTUAL.TXT
if [ $? -ne 0 ]; then
    fail "Application crashed during run — check input.txt and stack trace above"
fi
pass "Application run"

# ── step 3: resolve date placeholder ──────────────────────────────────────────
TODAY=$(date +%Y-%m-%d)
sed "s/{TODAY}/$TODAY/g" EXPECTED.TXT > EXPECTED-RESOLVED.TXT
if [ $? -ne 0 ]; then
    fail "Could not resolve {TODAY} placeholder in EXPECTED.TXT"
fi
pass "Date placeholder resolved ($TODAY)"

# ── step 4: normalise line endings ────────────────────────────────────────────
dos2unix EXPECTED-RESOLVED.TXT ACTUAL.TXT
if [ $? -ne 0 ]; then
    fail "dos2unix failed — is it installed?"
fi
pass "Line ending normalisation"

# ── step 5: diff ───────────────────────────────────────────────────────────────
diff EXPECTED-RESOLVED.TXT ACTUAL.TXT
if [ $? -ne 0 ]; then
    fail "Output mismatch — see diff above (< expected  > actual)"
fi

pass "All tests passed!"
exit 0