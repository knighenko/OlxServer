package entity;


public class Advertisement {
    private String title;
    private String url;
    private String imageSrc;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
/**Method return string - description of advertisement in JSON*/
    @Override
    public String toString() {
        StringBuilder stringBuffer = new StringBuilder("entity.Advertisement{");
        stringBuffer.append("title='").append(title).append('\'').append(", url='").append(url).append('\'').
                append(", imageSrc='").append(imageSrc).append('\'').append(", description='").append(description).append('\'').append('}');

        return stringBuffer.toString();
    }


}
