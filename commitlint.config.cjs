const { default: conventional } = require("@commitlint/config-conventional");

module.exports = {
  ...conventional,
  rules: {
    ...conventional.rules,
    // Repo types (see .releaserc): fix, feat, bump, perf trigger releases.
    "type-enum": [
      2,
      "always",
      [
        "build",
        "bump",
        "chore",
        "ci",
        "docs",
        "feat",
        "fix",
        "perf",
        "refactor",
        "revert",
        "style",
        "test",
      ],
    ],
  },
};
