# hdp-capability-registry-api

## Description
This repository contains the source code for the capability registry API. This API exposes the interface through which a consumer or provider interacts with the capability registry.  The interactions are register or unregister (provider) and list the capability of other providers (consumer). The API is written in JSD and generates a JAVA API.

## Documentation
You can find additional documentation for Project Symphony at [dellemc-symphony.readthedocs.io][documentation].

## Before you begin
Verify that the following tools are installed:
 
* Apache Maven 3.0.5+
* Java Development Kit (version 8)

## Building
Run the following command to build this project:
```bash
mvn clean install
```

## Contributing
Project Symphony is a collection of services and libraries housed at [GitHub][github].
 
Contribute code and make submissions at the relevant GitHub repository level. See [our documentation][contributing] for details on how to contribute.

## Community
Reach out to us on the Slack [#symphony][slack] channel by requesting an invite at [{code}Community][codecommunity].
 
You can also join [Google Groups][googlegroups] and start a discussion.
 
[slack]: https://codecommunity.slack.com/messages/symphony
[googlegroups]: https://groups.google.com/forum/#!forum/dellemc-symphony
[codecommunity]: http://community.codedellemc.com/
[contributing]: http://dellemc-symphony.readthedocs.io/en/latest/contributingtosymphony.html
[github]: https://github.com/dellemc-symphony
[documentation]: https://dellemc-symphony.readthedocs.io/en/latest/
