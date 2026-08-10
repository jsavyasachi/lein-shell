# Contributing

Contribute to lein-shell if you want to help maintain it.
These guidelines help contributors communicate and help maintainers review patches and fix reported bugs.

Send a patch if you are unsure whether it follows these guidelines.

## Issues

Report bugs, improvement ideas, questions, and other issues on the GitHub issue tracker.
Do not send bug reports to personal email addresses.

Include this information when it applies:

* What small set of steps reproduces the problem?
* What output do you expect? What output do you see instead?
* What version are you using?

## Patches

Send patches as GitHub pull requests.
Use topic branches instead of committing directly to master.
Use this commit message style:

* First line is 50 characters or less
* Then a blank line
* Remaining text should be wrapped at 72 characters

For example:

```bash
# Fork the project off Github
$ git clone git@github.com:your-username/lein-shell.git
$ cd lein-shell
$ git checkout -b my-patch
# Do your changes now, and stage them
$ lein test
$ git commit -m "I've fixed this and that, fixes #42."
$ git push
# Submit a pull request
```

## Code style

Follow the conventions in the existing code.
Avoid lines longer than 80 columns and function bodies longer than 20 lines.
Use `when` only for side-effects.

## Testing

Before you request a pull request, make sure your changes do not break existing test cases.
Add test coverage for changed functionality when possible.

Run `lein test` in the root directory to run the test cases.
