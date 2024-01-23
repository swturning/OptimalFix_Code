const solc = require('solc')
const fs = require('fs')
const shelljs = require('shelljs')
// const sourcePath = './contractSource/test/'
const sourcePath = 'C:/Users/paine/Desktop/对比试验/s1/'
const outputPath = './compileRes/TSE/'

// let fileidx = parseInt(process.argv.splice(2)[0])
let fileidx = process.argv.splice(2)[0]
console.log("fileidx",fileidx)
let files = fs.readdirSync(sourcePath)
console.log("files",files)


let inputTemplate = 
{
    language: 'Solidity',
    sources: {
    },
    settings: {
        outputSelection: {
            "*": {
              "*" : [],
              "": ["legacyAST"]
            }
          }
    }
};





for (let i = 0; i < files.length; i++) {
  const fileName = files[i];
  let p = i;
   p = p+1
  console.log("Processing file:", fileName+"  "+p);
  if(fileName == "undefined"||fileName=="Mine"||fileName=="假阳性") break;
  // 在这里执行文件处理的逻辑，你可以根据需要调用函数或执行其他操作
  // 例如，你可以在这里编译 Solidity 源文件，计算燃气消耗等

  // 示例：如果文件名以 ".sol" 扩展名结尾，执行某些操作
  console.log(fileidx)
  // let codePath = sourcePath + files[fileidx]
  //let codePath = sourcePath + fileidx
  let codePath = sourcePath + fileName
  
  let codeContent = fs.readFileSync(codePath, 'UTF-8').toString()
  

  let version = solc.version().toString().split('+')[0].split('.')[1]
  // discriminate the version of solc
  if(version !== '4'&&fileName !='undefined') {
    inputTemplate.sources[files[fileidx]] = {}
    inputTemplate.sources[files[fileidx]]['content'] = codeContent
    try {
      // console.log(inputTemplate)
      let output = JSON.parse(solc.compile(JSON.stringify(inputTemplate)));
      // console.log(output['contracts'].keys())
      
      // fs.writeFileSync(outputPath+files[fileidx].split('.')[0]+'.ast', JSON.stringify(output['sources'][files[fileidx]]['legacyAST']))
    } catch(e) {
      console.log(e.toString())
    }
  } else {
    try {
      debugger
     
      let output = solc.compile(codeContent, (res)=>{})
      console.log(output)
      let gasUsed = 0;
      for(key in output['contracts']){
       
          let gas_em = output['contracts'][key]['gasEstimates']
          
          let creation = gas_em['creation']
          
          let external = gas_em['external']
          let internal = gas_em['internal']
          
          for(gas in creation) {
              gasUsed += Number(creation[gas]);
          }
  
          for(func in external) {
              if(external[func]) {
                  gasUsed += Number(external[func])
              }
          }
  
          for(func in internal) {
              if(internal[func]) {
                  gasUsed += Number(internal[func])
              }
          }
      }
      console.log(gasUsed)
      // fs.writeFileSync(outputPath+files[fileidx].split('.')[0]+'.ast', JSON.stringify(output['sources']['']['AST']))
    } catch(e) {
      console.log(e.toString())
    }
  }
  
}



