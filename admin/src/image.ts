export async function optimizeImage(file:File){
  if(file.size>8*1024*1024)throw new Error('图片不能超过 8MB')
  if(!['image/jpeg','image/png','image/webp'].includes(file.type))throw new Error('只支持 JPG、PNG 和 WebP 图片')
  const bitmap=await createImageBitmap(file,{imageOrientation:'from-image'})
  try{
    const render=async(maxWidth:number,quality:number)=>{const scale=Math.min(1,maxWidth/bitmap.width);const canvas=document.createElement('canvas');canvas.width=Math.max(1,Math.round(bitmap.width*scale));canvas.height=Math.max(1,Math.round(bitmap.height*scale));canvas.getContext('2d')!.drawImage(bitmap,0,0,canvas.width,canvas.height);const blob=await new Promise<Blob>((resolve,reject)=>canvas.toBlob(value=>value?resolve(value):reject(new Error('浏览器无法压缩图片')),'image/webp',quality));return blob}
    const [main,thumbnail]=await Promise.all([render(1920,.82),render(480,.76)])
    const stem=file.name.replace(/\.[^.]+$/,'')||'image'
    return{file:new File([main],stem+'.webp',{type:'image/webp'}),thumbnail:new File([thumbnail],stem+'-thumb.webp',{type:'image/webp'})}
  }finally{bitmap.close()}
}
