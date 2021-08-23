checkbinaries:
	@which -s entr || (echo "[ERROR] You need to install 'entr' by running 'brew install entr' or equivalent for your platform"; exit 2)

build-test:
	@npx shadow-cljs watch test

mocha-watch:
	@mkdir -p target
	@rm -f target/compiled_test.js
	@touch target/compiled_test.js
	@ls target/compiled_test.js | entr npm run test

test-tasks: build-test mocha-watch

.PHONY: test
test:
	@${MAKE} checkbinaries
	@${MAKE} -j2 test-tasks
