package com.samlong.controller;

import com.samlong.model.NewsArticle;
import com.samlong.model.Product;
import com.samlong.model.Slide;
import com.samlong.repository.NewsRepository;
import com.samlong.repository.ProductRepository;
import com.samlong.repository.SlideRepository;
import com.samlong.service.AuditLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {
  private static final long MAX_BYTES=8L*1024L*1024L;
  private static final long MAX_THUMB_BYTES=2L*1024L*1024L;
  private static final long MAX_PIXELS=40000000L;
  private static final Set<String> ALLOWED_TYPES=new HashSet<String>(Arrays.asList("image/jpeg","image/png","image/webp"));
  @Value("${app.upload.dir:uploads}") private String uploadDir;
  private final ProductRepository products;private final NewsRepository news;private final SlideRepository slides;private final AuditLogService audit;

  public UploadController(ProductRepository products,NewsRepository news,SlideRepository slides,AuditLogService audit){this.products=products;this.news=news;this.slides=slides;this.audit=audit;}

  @PostMapping
  public Map<String,Object> upload(@RequestParam("file") MultipartFile file,@RequestParam(value="thumbnail",required=false) MultipartFile thumbnail) throws IOException {
    String type=validate(file,MAX_BYTES);String id=UUID.randomUUID().toString().replace("-","");File directory=directory();String extension=extension(type);File output=new File(directory,id+extension);file.transferTo(output);
    File thumb=null;if(thumbnail!=null&&!thumbnail.isEmpty()){String thumbType=validate(thumbnail,MAX_THUMB_BYTES);thumb=new File(directory,id+"-thumb"+extension(thumbType));thumbnail.transferTo(thumb);}
    else if(!"image/webp".equals(type)){BufferedImage source=ImageIO.read(output);if(source!=null){thumb=new File(directory,id+"-thumb"+extension);ImageIO.write(resize(source,480),"image/png".equals(type)?"png":"jpg",thumb);}}
    String url="/uploads/"+output.getName();audit.record("CREATE","MEDIA",null,output.getName());Map<String,Object> result=new LinkedHashMap<String,Object>();result.put("url",url);result.put("thumbnailUrl",thumb==null?url:"/uploads/"+thumb.getName());result.put("originalSize",file.getSize());result.put("size",output.length());return result;
  }

  @GetMapping
  public List<Map<String,Object>> list(){File[] files=directory().listFiles(pathname->pathname.isFile()&&!pathname.getName().matches(".*-thumb\\.(webp|jpg|jpeg|png)$")&&isImageName(pathname.getName()));if(files==null)return Collections.emptyList();Arrays.sort(files,(a,b)->Long.compare(b.lastModified(),a.lastModified()));List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();for(File file:files){File thumb=findThumbnail(file);String url="/uploads/"+file.getName();Map<String,Object> item=new LinkedHashMap<String,Object>();item.put("name",file.getName());item.put("url",url);item.put("thumbnailUrl",thumb==null?url:"/uploads/"+thumb.getName());item.put("size",file.length());item.put("createdAt",Instant.ofEpochMilli(file.lastModified()).toString());item.put("used",isUsed(url));result.add(item);}return result;}

  @DeleteMapping("/{filename:.+}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String filename) throws IOException {if(!filename.matches("[A-Za-z0-9._-]+")||filename.matches(".*-thumb\\..*"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"文件名不正确");File file=new File(directory(),filename);String url="/uploads/"+filename;if(!file.exists())throw new ResponseStatusException(HttpStatus.NOT_FOUND,"图片不存在");if(isUsed(url))throw new ResponseStatusException(HttpStatus.CONFLICT,"图片正在被官网内容使用，不能删除");Files.delete(file.toPath());deleteThumbnails(file);audit.record("DELETE","MEDIA",null,filename);}

  private String validate(MultipartFile file,long maxBytes) throws IOException {String type=file.getContentType()==null?"":file.getContentType().toLowerCase();if(file.isEmpty()||!ALLOWED_TYPES.contains(type))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"只支持 JPG、PNG 和 WebP 图片");if(file.getSize()>maxBytes)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"图片文件过大");if("image/webp".equals(type)){byte[] header=new byte[12];try(InputStream input=file.getInputStream()){if(input.read(header)!=12||!new String(header,0,4,"US-ASCII").equals("RIFF")||!new String(header,8,4,"US-ASCII").equals("WEBP"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"WebP 图片内容不正确");}}else{BufferedImage image;try(InputStream input=file.getInputStream()){image=ImageIO.read(input);}if(image==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"无法识别图片内容");if((long)image.getWidth()*(long)image.getHeight()>MAX_PIXELS)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"图片像素尺寸过大");}return type;}
  private boolean isUsed(String url){for(Product item:products.findAll()){if(url.equals(item.getImageUrl())||item.getImageUrls().contains(url))return true;}for(NewsArticle item:news.findAll())if(url.equals(item.getImageUrl()))return true;for(Slide item:slides.findAll())if(url.equals(item.getImageUrl()))return true;return false;}
  private File directory(){File value=new File(uploadDir).getAbsoluteFile();if(!value.exists()&&!value.mkdirs())throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"无法创建上传目录");return value;}
  private String extension(String type){if("image/png".equals(type))return ".png";if("image/webp".equals(type))return ".webp";return ".jpg";}
  private boolean isImageName(String name){String value=name.toLowerCase();return value.endsWith(".jpg")||value.endsWith(".jpeg")||value.endsWith(".png")||value.endsWith(".webp");}
  private File findThumbnail(File file){String stem=stem(file);for(String extension:new String[]{".webp",".jpg",".jpeg",".png"}){File candidate=new File(file.getParentFile(),stem+"-thumb"+extension);if(candidate.exists())return candidate;}return null;}
  private void deleteThumbnails(File file) throws IOException {String stem=stem(file);for(String extension:new String[]{".webp",".jpg",".jpeg",".png"})Files.deleteIfExists(new File(file.getParentFile(),stem+"-thumb"+extension).toPath());}
  private String stem(File file){String name=file.getName();int dot=name.lastIndexOf('.');return dot>0?name.substring(0,dot):name;}
  private BufferedImage resize(BufferedImage source,int maxWidth){if(source.getWidth()<=maxWidth)return source;int width=maxWidth;int height=Math.max(1,(int)Math.round(source.getHeight()*(maxWidth/(double)source.getWidth())));BufferedImage target=new BufferedImage(width,height,source.getColorModel().hasAlpha()?BufferedImage.TYPE_INT_ARGB:BufferedImage.TYPE_INT_RGB);Graphics2D graphics=target.createGraphics();graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);graphics.drawImage(source,0,0,width,height,null);graphics.dispose();return target;}
}
