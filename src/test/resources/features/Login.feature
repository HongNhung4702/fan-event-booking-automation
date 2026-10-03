@ui @login @saucedemo
Feature: Đăng nhập vào SauceDemo
  Với tư cách là người dùng SauceDemo
  Tôi muốn đăng nhập vào hệ thống
  Để có thể truy cập hệ thống khi thông tin đăng nhập hợp lệ
  Và nhận được phản hồi phù hợp khi dữ liệu không hợp lệ

  Background:
    Given người dùng mở trang đăng nhập SauceDemo
    Then biểu mẫu đăng nhập SauceDemo phải được hiển thị


  @positive @high
  Scenario: SD_LOGIN_001 - Đăng nhập thành công với dữ liệu hợp lệ
    When người dùng đăng nhập bằng bộ dữ liệu "SD_LOGIN_001"
    Then kết quả đăng nhập phải đúng với bộ dữ liệu "SD_LOGIN_001"
    And người dùng kiểm tra các thông tin sau:
      | thuộc tính |
      | URL        |
      | tiêu đề    |


  @negative @high
  Scenario Outline: <testCaseId> - Kiểm thử dữ liệu đăng nhập không hợp lệ
    When người dùng đăng nhập bằng bộ dữ liệu "<testCaseId>"
    Then kết quả đăng nhập phải đúng với bộ dữ liệu "<testCaseId>"

    Examples:
      | testCaseId   |
      | SD_LOGIN_002 |
      | SD_LOGIN_003 |
      | SD_LOGIN_004 |
      | SD_LOGIN_005 |
      | SD_LOGIN_006 |
      | SD_LOGIN_007 |


  @edge @medium
  Scenario Outline: <testCaseId> - Kiểm thử dữ liệu biên của chức năng đăng nhập
    When người dùng đăng nhập bằng bộ dữ liệu "<testCaseId>"
    Then kết quả đăng nhập phải đúng với bộ dữ liệu "<testCaseId>"

    Examples:
      | testCaseId   |
      | SD_LOGIN_008 |
      | SD_LOGIN_009 |
      | SD_LOGIN_010 |