# Instructions for use

OptimalFix an integrated automated framework that combines intelligent contract vulnerability detection and repair. It is built as a Maven project using Java JDK 1.8.



## The steps for using 

1. The **prettier-plugin-solidity** code formatting tool needs to be installed in the contractsPath directory.

   Run the following installation command in the command line under the directory:

```shell
npm install --save-dev prettier prettier-plugin-solidity

```

After installation, the   **.prettierignore** file   and   **.prettierrc.js**   in the original contractsPath directory are the configuration files for **prettier**. 

2.  This tool is a Maven project and can be built by executing the following command:

   ```shell
   $ mvn clean package
   ```

A jar file will be generated in the target directory. Since the project is still in the update phase, it is recommended to place the jar in the main directory        for execution.

3. Execute the following command to run:

   ```shell
   $ java -jar your_jar_name.jar -p  <path of the contract>  -r XPath/solidity-rules
      eg: java -jar OptimalFix-jar-with-dependencies.jar -p test/simpleTest.sol  -r XPath/solidity-rules
   ```

   It is recommended to place the contract to be tested in the `test` directory under the project directory. The parameter following `-r` is for the rule library and does not need to be modified by default.

4. After running, the detected vulnerabilities and corresponding repair results will be displayed. The repaired contract will be placed in the test/patch folder.

