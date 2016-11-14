#!/bih/sh

# Periodically, at least on non-Windows platforms, Git will declare that thousands of files
# in this repo have been changed completely, with every line considered changed and then
# replaced with an identical one. This seems to be due to LF/CRLF handling, and has proven
# to crop up as a recurring problem. Running this command should clear it up.
# IMPORTANT: only run this command when you have no real pending changes - it will
# hard-reset any changed files to the last commit.

set -e;

git rm --cached -r .;
git reset --hard;