Reduces the size of the errors from the narrator.

The mod hides stacktraces and the path from the narrator error, reducing the `flite` error by 98%¹

It's config allows you to customize the error details:
- `hide_stacktraces` - Removes stacktraces² from the logs (Default: `true`)
- `hide_path` - Removes the path from the error message (Default: `true`)
- `hide_suppressed` - Replaces the exception thrown to hide the supressed exceptions from the original one (Default: `true`)
- `disable_narrator` - Enable to completely disable the narrator. This logs a warning to avoid confusion on why the narrator is missing. (Default: `false`)

The mod is also very small and stable, if a newer version is not listed, it is likely that it will still work

##### ¹Calculated from the log printed on development env
