# Contributing to Wolf Armor and Storage

Hey there!  Thanks for considering contributing to Wolf Armor and Storage!

This is a set of guidelines for contributing to Wolf Armor and Storage.  Please remember that these are primarily guidelines, not hard rules, so feel free to use your own judgement.  Also feel free to propose changes to these guidelines in a new pull request.  Any input is welcome!

## Table of Contents

* [Code of Conduct](#code-of-conduct)
* [FAQ](#faq)
  * [Updating to newer Minecraft versions](#please-update-to-mcxxx)
  * [Modpack permissions](#can-i-use-this-in-my-modpack)
  * [Other questions](#something-else)
* [Contributing](#contributing)
  * [Reporting bugs](#reporting-bugs)
    * [Before submitting a bug report](#before-submitting-a-bug-report)
    * [Writing a good bug report](#writing-a-good-bug-report)
  * [Suggesting enhancements](#suggesting-enhancements)
    * [Before submitting a new request](#before-submitting-a-new-request)
    * [Submitting a good enhancement request](#submitting-a-good-enhancement-request)
  * [Your first code contribution](#your-first-code-contribution)
  * [Pull requests](#pull-requests)
* [Style Guide](#style-guide)
  * [Git commit messages](#git-commit-messages)
  * [Java style guide](#java-style-guide)
  * [Groovy style guide](#groovy-style-guide)
  * [Documentation style guides](#documentation-style-guides)
* [Additional notes](#additional-notes)

## Code of Conduct

This project and all of its participants are governed by the contributor [Code of Conduct](CODE_OF_CONDUCT.md).  While contributing you are expected to uphold this code of conduct.  Behavior which violates the Code can be reported to [reports@satyrn.dev](mailto:reports@satyrn.dev).  Any received reports are anonymous, and will be looked into as soon as possible.

## FAQ

### Please update to MCX.X.X!!!!!!

This gets asked a lot.  Porting to a different Minecraft version is a lot of work, and before that process begins all issues and feature requests should be completed to reduce time spent merging.  It will be updated eventually, don't worry!

### Can I use this in my modpack?

As long as your modpack is at least compliant with the [Code of Conduct](CODE_OF_CONDUCT.md), then it is fine to incorporate this mod.

### Something else?

Please ask any questions that aren't answered here in the [CurseForge Comments Section](https://www.curseforge.com/minecraft/mc-mods/wolf-armor-and-storage#comments).

## Contributing

### Reporting bugs

This section will take you through the process of submitting a bug report.  Following these guidelines helps maintainers understand, reproduce, and fix your issue.

#### Before submitting a bug report

Before creating a bug, check the [existing issues](https://github.com/satyrnidae/wolfarmor/issues) to make sure your bug hasn't already been reported.  If you find an open issue which matches your bug, leave a comment instead of adding a new report.  However, if you find a closed issue which matches your bug, create a new issue and link the old one.

> Note: Questions are not issues, and you should not create an issue just to ask a question. Please direct your questions to the [CurseForge Comments Section](https://www.curseforge.com/minecraft/mc-mods/wolf-armor-and-storage#comments).

#### Writing a good bug report

Bugs are tracked via [Github issues](https://guides.github.com/features/issues/).  When you create an issue please use the [existing template](https://github.com/satyrnidae/wolfarmor/tree/master/.github/ISSUE_TEMPLATE/bug-report.md), as it makes it easier to categorize and fix issues.

When filling out the template, be sure to:
* **Use a clear and descriptive title** to identify the issue.  Include the Minecraft version in brackets at the start of the title.
* **Describe the behavior you observed** and point out why the behavior is incorrect.
* **Describe the exact steps to reproduce the problem** in as many details as possible.  Don't simply say what you did, **explain how you did it**.  Providing these details makes it much easier to reproduce the issue.
* **Explain the behavior you expected** to occur.
* **Include screenshots or GIFs** showing the problem as it is occurring.

Include details about your environment:

* **What operating system** are you running the mod on when the issue occurs?
* **Which Java release** is Minecraft being executed in? Is it the JRE or the JDK?
* **Which Minecraft version** are you running?
* **What version of Forge** are you using?
* **On which version of the mod** did you encounter this problem?  Did it just recently start with a new release?

Provide any additional context:

* If reporting a crash, **include a link to the crash log**, whether it be in a gist or on a text sharing site such as PasteBin.  *Please don't put full crash logs in your issue descriptions.*
* **Can you reliably reproduce the issue?**  If not, please provide details about how often the problem occurs, and under which conditions it normally happens.
* **Does the issue still occur when only Wolf Armor and Storage is loaded?** What other mods are you running?  If you're reporting a crash your mod list should be included in the crash logs, so make sure to provide that gist link!

### Suggesting enhancements

This section will take you through the process of submitting an enhancement request.  Following these guidelines ensures your enhancement will be well explained, as well as make it easier to find related suggestions.

#### Before submitting a new request

Before creating an enhancement request, check the [existing issues](https://github.com/satyrnidae/wolfarmor/issues) to make sure a similar enhancement has not already been proposed.  If it has, please leave a comment on the existing request instead of submitting a new one

#### Submitting a good enhancement request

Enhancement requests are tracked via [Github issues](https://guides.github.com/features/issues/).  When you create an issue please use the [existing template](https://github.com/satyrnidae/wolfarmor/tree/master/.github/ISSUE_TEMPLATE/feature_request.md), as it makes it easier to categorize them.

When filling out the template, be sure to:
* **Use a clear and concise title** to identify the issue.
* **Describe the solution** clearly and concisely.
* **Provide details** such as AI changes, configuration settings, new items, entity functionality, etc.
* **Describe why the enhancement is desirable**, and how adding the new functionality will improve the mod
* **Describe alternative solutions** and why you chose the solution that you have (if applicable).
* **Attach mockup screenshots, videos, and / or diagrams** to help visualize the solution
* **Provide additional context**, such as mods where similar functionality is implemented, to help with understanding the enhancement

### Your first code contribution

When beginning your first code contribution, you way want to refer to the following lists:
* [Good first issues](https://github.com/satyrnidae/wolfarmor/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22) - Simple issues perfect for beginners
* [Help wanted](https://github.com/satyrnidae/wolfarmor/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22) - Slightly more complex issues looking for contributions

### Pull requests

When submitting pull requests, please be sure to follow these guidelines:

* Fill out the [template](https://github.com/satyrnidae/wolfarmor/tree/master/.github/ISSUE_TEMPLATE/feature_request.md)
* Follow the [style guidelines](#style-guidelines)
* Ensure that the [build status](https://github.com/satyrnidae/wolfarmor/actions) for your branch is currently "passing"

If your pull request is not being merged into the latest version, please make sure to target the correct ``versions`` branch instead of ``master`` (e.g. ``versions/1.12``)

## Style guidelines

### Git commit messages

* Use the present tense ("Update README", not "Updated README")
* Use the imperative mood ("Move file" not "Moves file")
* Limit the first line to ⩽50 characters
* Link any applicable issue
* Consider prefixing your commits with an emoji, following the [gitmoji](https://gitmoji.carloscuesta.me/) guidelines

### Java style guide

* Use 4 spaces to indent lines
* Place annotations on their own lines
  * If the method is a single line getter/ setter, you can place annotations on the same line (see example below)
* Place simple getters / setters on a single line
  ```java
  @Override public boolean isValid() { return this.valid; }
  ```
* Place opening brackets on the same line
  ```java
  private static Entity create() {
    // etc.
  }
* PascalCase for classes, camelCase for methods

### Groovy style guide

Wolf Armor uses groovy and the spock framework for unit tests.  Follow the [Java style guide](#java-style-guides)

### Documentation style guides

* Use [JavaDoc](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html) to document classes and methods
* Include ``@author`` and ``@since`` in class comments
* Include ``@since`` in function comments

## Additional Notes

### Issue labels

The following labels are currently in use in the Wolf Armor project.  It is recommended to correctly label issues / enhancement requests / pull requests when you submit them.

| Label | Results | Description |
| - | - | - |
| 1.12 | [link](https://github.com/satyrnidae/wolfarmor/labels/1.12) | Issues affecting Minecraft version 1.12 |
| 1.14 | [link](https://github.com/satyrnidae/wolfarmor/labels/1.14) | Issues affecting Minecraft version 1.14 |
| branch | [link](https://github.com/satyrnidae/wolfarmor/labels/branch) | Issues requiring the creation of a new branch |
| bug | [link](https://github.com/satyrnidae/wolfarmor/labels/bug) | Bug reports |
| build | [link](https://github.com/satyrnidae/wolfarmor/labels/build) | Issues related to build files (CI and Gradle) |
| can't reproduce | [link](https://github.com/satyrnidae/wolfarmor/labels/can%27t%20reproduce) | Issues that could not be reproduced by a developer |
| compatibility | [link](https://github.com/satyrnidae/wolfarmor/labels/compatibility) | Issues caused by interactions with other mobs |
| CRITICAL | [link](https://github.com/satyrnidae/wolfarmor/labels/CRITICAL) | Critical issues that require an immediate fix |
| duplicate | [link](https://github.com/satyrnidae/wolfarmor/labels/duplicate) | Issues that were closed as duplicates |
| enhancement | [link](https://github.com/satyrnidae/wolfarmor/labels/enhancement) | Enhancement requests |
| good first issue | [link](https://github.com/satyrnidae/wolfarmor/labels/good%20first%20issue) | Issues which are good for beginners and new contributors |
| help wanted | [link](https://github.com/satyrnidae/wolfarmor/labels/help%20wanted) | Issues that are requesting help from contributors |
| investigation | [link](https://github.com/satyrnidae/wolfarmor/labels/investigation) | Issues that require further analysis |
| not an issue | [link](https://github.com/satyrnidae/wolfarmor/labels/not%20an%20issue) | Questions, requests for config help, and spam |
| versioning | [link](https://github.com/satyrnidae/wolfarmor/labels/versioning) | Issues related to new Minecraft versions |
| wontfix | [link](https://github.com/satyrnidae/wolfarmor/labels/wontfix) | Rejected issues |
