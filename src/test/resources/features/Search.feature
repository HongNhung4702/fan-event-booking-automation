@ui @search @automationexercise
Feature: Tìm kiếm sản phẩm trên Automation Exercise
  Với tư cách là người dùng Automation Exercise
  Tôi muốn tìm kiếm sản phẩm
  Để có thể tìm được sản phẩm mong muốn
  Và hệ thống vẫn ổn định với các dữ liệu biên

  Background:
    Given người dùng mở trang chủ Automation Exercise
    When người dùng chọn menu Products
    Then trang ALL PRODUCTS phải được hiển thị


  @positive @high
  Scenario Outline: <testCaseId> - Tìm kiếm sản phẩm với dữ liệu hợp lệ
    When người dùng tìm kiếm bằng bộ dữ liệu "<testCaseId>"
    Then kết quả tìm kiếm phải đúng với bộ dữ liệu "<testCaseId>"
    And người dùng kiểm tra các thành phần:
      | thành phần        |
      | SEARCHED PRODUCTS |
      | danh sách sản phẩm|

    Examples:
      | testCaseId    |
      | AE_SEARCH_001 |
      | AE_SEARCH_002 |
      | AE_SEARCH_003 |
      | AE_SEARCH_004 |
      | AE_SEARCH_005 |


  @negative @high
  Scenario: AE_SEARCH_006 - Tìm kiếm sản phẩm không tồn tại
    When người dùng tìm kiếm bằng bộ dữ liệu "AE_SEARCH_006"
    Then kết quả tìm kiếm phải đúng với bộ dữ liệu "AE_SEARCH_006"


  @edge @medium
  Scenario Outline: <testCaseId> - Kiểm thử dữ liệu biên của chức năng tìm kiếm
    When người dùng tìm kiếm bằng bộ dữ liệu "<testCaseId>"
    Then hệ thống tìm kiếm phải ổn định với bộ dữ liệu "<testCaseId>"

    Examples:
      | testCaseId    |
      | AE_SEARCH_007 |
      | AE_SEARCH_008 |
      | AE_SEARCH_009 |
      | AE_SEARCH_010 |
      | AE_SEARCH_011 |
      | AE_SEARCH_012 |
      | AE_SEARCH_013 |


  @positive @high
  Scenario: AE_SEARCH_014 - Thực hiện hai lần tìm kiếm liên tiếp
    When người dùng tìm kiếm bằng bộ dữ liệu "AE_SEARCH_014"
    Then kết quả tìm kiếm phải đúng với bộ dữ liệu "AE_SEARCH_014"

    When người dùng tìm kiếm bằng bộ dữ liệu "AE_SEARCH_004"
    Then kết quả tìm kiếm phải đúng với bộ dữ liệu "AE_SEARCH_004"


  @AE_SEARCH_015 @positive @medium
  Scenario: AE_SEARCH_015 - Mở chi tiết sản phẩm từ kết quả tìm kiếm