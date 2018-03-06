package com.hls.plugins.barcode.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by panx on 2017/3/15.
 */
public class HttpInfoBean implements Parcelable{

  private String url;
  private String method;
  private Headers headers;
  private String data;
  private Config config;

  public HttpInfoBean() {

  }

  /**
   * 请求头
   */
  public static class Headers implements Parcelable {
    private String authorization;
    private String contentType;

    public Headers() {

    }

    protected Headers(Parcel in) {
      authorization = in.readString();
      contentType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(authorization);
      dest.writeString(contentType);
    }

    @Override
    public int describeContents() {
      return 0;
    }

    public static final Creator<Headers> CREATOR = new Creator<Headers>() {
      @Override
      public Headers createFromParcel(Parcel in) {
        return new Headers(in);
      }

      @Override
      public Headers[] newArray(int size) {
        return new Headers[size];
      }
    };

    public String getAuthorization() {
      return authorization;
    }

    public void setAuthorization(String authorization) {
      this.authorization = authorization;
    }

    public String getContentType() {
      return contentType;
    }

    public void setContentType(String contentType) {
      this.contentType = contentType;
    }
  }

  /**
   * 配置
   */
  public static class Config implements Parcelable {
    private int timeout;

    public Config() {
    }

    protected Config(Parcel in) {
      timeout = in.readInt();
    }

    public static final Creator<Config> CREATOR = new Creator<Config>() {
      @Override
      public Config createFromParcel(Parcel in) {
        return new Config(in);
      }

      @Override
      public Config[] newArray(int size) {
        return new Config[size];
      }
    };

    public int getTimeout() {
      return timeout;
    }

    public void setTimeout(int timeout) {
      this.timeout = timeout;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeInt(timeout);
    }
  }

  protected HttpInfoBean(Parcel in) {
    url = in.readString();
    method = in.readString();
    headers = in.readParcelable(Headers.class.getClassLoader());
    data = in.readString();
    config = in.readParcelable(Config.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(url);
    dest.writeString(method);
    dest.writeParcelable(headers, flags);
    dest.writeString(data);
    dest.writeParcelable(config, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<HttpInfoBean> CREATOR = new Creator<HttpInfoBean>() {
    @Override
    public HttpInfoBean createFromParcel(Parcel in) {
      return new HttpInfoBean(in);
    }

    @Override
    public HttpInfoBean[] newArray(int size) {
      return new HttpInfoBean[size];
    }
  };

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  public Headers getHeaders() {
    return headers;
  }

  public void setHeaders(Headers headers) {
    this.headers = headers;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
