@echo off
setlocal enableextensions
pushd %~dp0

cd ..

:: ── step 1: build ─────────────────────────────────────────────────────────────
call gradlew clean shadowJar
if errorlevel 1 (
    echo [FAIL] Build failed -- gradlew clean shadowJar did not complete successfully
    exit /b 1
)
echo [PASS] Build

:: ── step 2: locate jar ────────────────────────────────────────────────────────
cd build\libs
set jarloc=
for /f "tokens=*" %%a in ('dir /b *.jar 2^>nul') do set jarloc=%%a

if "%jarloc%"=="" (
    echo [FAIL] No jar found in build\libs\
    exit /b 1
)
echo [PASS] Jar located: %jarloc%

:: ── step 3: run app ───────────────────────────────────────────────────────────
java -jar %jarloc% < ..\..\text-ui-test\input.txt > ..\..\text-ui-test\ACTUAL.TXT
if errorlevel 1 (
    echo [FAIL] Application crashed during run -- check input.txt and output above
    exit /b 1
)
echo [PASS] Application run

:: ── step 4: resolve date placeholder ─────────────────────────────────────────
cd ..\..\text-ui-test

for /f "tokens=1-3 delims=-" %%a in (
    'powershell -NoProfile -Command "Get-Date -Format yyyy-MM-dd"'
) do set TODAY=%%a-%%b-%%c

powershell -NoProfile -Command ^
    "(Get-Content EXPECTED.TXT) -replace '\{TODAY\}', '%TODAY%' | Set-Content EXPECTED-RESOLVED.TXT"
if errorlevel 1 (
    echo [FAIL] Could not resolve {TODAY} placeholder in EXPECTED.TXT
    exit /b 1
)
echo [PASS] Date placeholder resolved (%TODAY%^)

:: ── step 5: diff ──────────────────────────────────────────────────────────────
FC ACTUAL.TXT EXPECTED-RESOLVED.TXT
if errorlevel 1 (
    echo [FAIL] Output mismatch -- see diff above
    exit /b 1
)

echo [PASS] All tests passed!