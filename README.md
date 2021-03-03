A simple CorDapp and client, used for manual testing when making changes to the `corda` repo.

This repository should be downloaded and copied into the `corda` directory, and its modules (`joel`, `joel:cordapp` and `joel:client`) should be included in the `settings.gradle` file.

Things to do:

* Run the `joel:deployNodes` task to create the nodes
* Run the `joel:cordapp:cpk` task to generate the CorDapp CPK
* Run the `joel:client:run` task to interact with a running node