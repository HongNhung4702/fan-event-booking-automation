@login @saucedemo
Feature: Đăng nhập vào SauceDemo
  Với tư cách là người dùng SauceDemo
  Tôi muốn đăng nhập bằng tài khoản của mình
  Để có thể truy cập trang Products khi thông tin đăng nhập hợp lệ
  Và nhận được thông báo phù hợp khi thông tin đăng nhập không hợp lệ

  Background:
    Given người dùng mở trang đăng nhập SauceDemo
    And người dùng chưa đăng nhập

  @SD_LOGIN_001 @positive @high
  Scenario: SD_LOGIN_001 - Đăng nhập thành công với tài khoản hợp lệ
    When người dùng nhập username "standard_user"
    And người dùng nhập password "secret_sauce"
    And người dùng nhấn nút "Login"
    Then người dùng đăng nhập thành công
    And URL hiện tại phải chứa "/inventory.html"
    And trang "Products" phải được hiển thị

  @SD_LOGIN_002 @negative @high
  Scenario: SD_LOGIN_002 - Đăng nhập với username không tồn tại
    When người dùng nhập username "invalid_user"
    And người dùng nhập password "secret_sauce"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username and password do not match any user in this service" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_003 @negative @high
  Scenario: SD_LOGIN_003 - Đăng nhập với password sai
    When người dùng nhập username "standard_user"
    And người dùng nhập password "wrong_password"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username and password do not match any user in this service" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_004 @negative @high
  Scenario: SD_LOGIN_004 - Bỏ trống Username
    When người dùng để trống trường username
    And người dùng nhập password "secret_sauce"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username is required" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_005 @negative @high
  Scenario: SD_LOGIN_005 - Bỏ trống Password
    When người dùng nhập username "standard_user"
    And người dùng để trống trường password
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Password is required" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_006 @negative @high
  Scenario: SD_LOGIN_006 - Bỏ trống cả Username và Password
    When người dùng để trống trường username
    And người dùng để trống trường password
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username is required" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_007 @negative @high
  Scenario: SD_LOGIN_007 - Đăng nhập bằng tài khoản bị khóa
    When người dùng nhập username "locked_out_user"
    And người dùng nhập password "secret_sauce"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Sorry, this user has been locked out." phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_008 @edge @medium
  Scenario: SD_LOGIN_008 - Username phân biệt chữ hoa và chữ thường
    When người dùng nhập username "STANDARD_USER"
    And người dùng nhập password "secret_sauce"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username and password do not match any user in this service" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_009 @edge @medium
  Scenario: SD_LOGIN_009 - Password phân biệt chữ hoa và chữ thường
    When người dùng nhập username "standard_user"
    And người dùng nhập password "SECRET_SAUCE"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username and password do not match any user in this service" phải được hiển thị
    And người dùng vẫn ở trang Login

  @SD_LOGIN_010 @edge @medium
  Scenario: SD_LOGIN_010 - Username có khoảng trắng ở đầu và cuối
    When người dùng nhập username " standard_user "
    And người dùng nhập password "secret_sauce"
    And người dùng nhấn nút "Login"
    Then người dùng không được đăng nhập
    And thông báo lỗi "Epic sadface: Username and password do not match any user in this service" phải được hiển thị
    And người dùng vẫn ở trang Login