Go to the directory where you want to generate the stubs and create a "build" directory and a "src" directory.

All of the generated source code will go under "src" folder.

If you are using Eclipse 3.2, create a custom binding to disable "Wrapper Style" code generation.

<jaxws:bindings wsdlLocation="http://ecs.amazonaws.com/AWSECommerceService/AWSECommerceService.wsdl" xmlns:jaxws="http://java.sun.com/xml/ns/jaxws">
  <jaxws:enableWrapperStyle>false</jaxws:enableWrapperStyle>
</jaxws:bindings>
This step is necessary because Eclipse 3.2 does not support wrapper style generated code. However, if you are an IDE that does support wrapper style generated code, such as NetBeans, this step is not required.

Run the command:

wsimport -d ./build -s ./src  -p com.ECS.client.jax http://ecs.amazonaws.com/AWSECommerceService/AWSECommerceService.wsdl -b jaxws-custom.xml . 
You can find the generated stubs in the path, com.ECS.client.jax .

Generated File Types

Several file types are generated in the package, com.ECS.client.jax:

AWSECommerceService—This file identifies the Product Advertising API service.

AWSECommerceServicePortType—This file provides the port type that the client can listen on.

This file also contains a list of all Product Advertising API operation signatures that can be used to build the client.


Optionally, you can next create the jar file with the following command: jar cvf amazon-ecs.jar -C build/ .


Information on how to use amazon ecs is at http://docs.amazonwebservices.com/AWSECommerceService/latest/GSG/index.html?YourDevelopmentEnvironment.html
