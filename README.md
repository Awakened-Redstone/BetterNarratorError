Reduces the size of the errors from the narrator.

The mod hides stacktraces and the path from the narrator error, reducing the `flite` error by 98%¹

It's config allows you to customize the error details:
- `hide_stacktraces` - Removes stacktraces from the logs (Default: `true`)
- `hide_path` - Removes the system path from the error message (Default: `true`)
- `hide_suppressed` - Hides suppressed exceptions from the logs (Default: `true`)
- `disable_narrator` - Completely disables the narrator (Default: `false`)

Disabling the narrator logs a warning to avoid confusion on why the narrator is missing.

The mod is very small and stable, if a newer version is not listed, it is likely that it will still work

##### ¹Calculated from the log printed on development env
