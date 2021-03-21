# GithubIssueTracker-Kotlin
[![GitHub stars](https://img.shields.io/github/stars/SteveKamau72/GithubIssueTracker---Kotlin)](https://github.com/SteveKamau72/GithubIssueTracker---Kotlin/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/SteveKamau72/GithubIssueTracker---Kotlin)](https://github.com/SteveKamau72/GithubIssueTracker---Kotlin/network)
[![GitHub issues](https://img.shields.io/github/issues/SteveKamau72/GithubIssueTracker---Kotlin)](https://github.com/SteveKamau72/GithubIssueTracker---Kotlin/issues)
![GitHub](https://img.shields.io/github/license/stevekamau72/GithubIssueTracker---Kotlin)

This is a project assignment. These were the requirements:

1. The code written for this assignment should be placed on your personal Github/Gitlab.

2. Prototype,wireframe, design and build a GITHUB ISSUE TRACKER mobile app using Flutter / native (Java/Kotlin/Swift/Obj-C) or React Native.

3. The app should; 
- talk to the open Github GraphQL API
- use a state management framework. Specifically, redux
- have filters, search and tags implementation
- come with automated tests (unit, integration and end-to-end/UI)
- be downloadable from play store or app store. If you do not have a Play or App store account, you can use a testing service e.g Firebase App Distribution
- For inspiration, consider [this design by Willard Shikami](https://dribbble.com/shots/14624938-Flutter-Issue-Tracker-UI)

## To run this project
Create an access token to GitHub API. On your profile, go to Settings, at the left side of the page click at Developer Settings, then Personal access tokens, and finally click on Generate new token button.

Add the token to ```gradle.properties```


```bash
GITHUB_KEY = "PASTE YOUR GITHUB_PERSONAL_TOKEN"
```

## Customize

The current repository this project is using to fetch the issues from is [TensorFlow](https://github.com/tensorflow/tensorflow).

You can change this from ```utils/Constants```

```kotlin
   const val REPO_NAME: String = "tensorflow"
   const val REPO_OWNER: String = "tensorflow"
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT License](https://choosealicense.com/licenses/mit/)
MIT License
