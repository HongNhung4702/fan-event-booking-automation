@search @automationexercise
Feature: Tìm kiếm sản phẩm trên Automation Exercise
  Với tư cách là người dùng Automation Exercise
  Tôi muốn tìm kiếm sản phẩm theo từ khóa
  Để có thể nhanh chóng tìm được sản phẩm phù hợp
  Và hệ thống vẫn hoạt động ổn định với dữ liệu không hợp lệ hoặc dữ liệu biên

  Background:
    Given người dùng mở trang Automation Exercise
    When người dùng chọn menu "Products"
    Then trang "ALL PRODUCTS" phải được hiển thị

  @AE_SEARCH_001 @positive @high
  Scenario: AE_SEARCH_001 - Tìm kiếm bằng tên sản phẩm chính xác
    When người dùng nhập từ khóa "Blue Top" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And sản phẩm "Blue Top" phải xuất hiện trong kết quả tìm kiếm

  @AE_SEARCH_002 @positive @high
  Scenario: AE_SEARCH_002 - Tìm kiếm bằng một phần tên sản phẩm
    When người dùng nhập từ khóa "Top" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And danh sách kết quả phải chứa các sản phẩm liên quan đến từ khóa "Top"
    And kết quả có thể bao gồm các sản phẩm như "Blue Top", "Winter Top" hoặc "Summer White Top"

  @AE_SEARCH_003 @positive @high
  Scenario: AE_SEARCH_003 - Tìm kiếm từ khóa Dress có nhiều kết quả
    When người dùng nhập từ khóa "Dress" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And danh sách kết quả phải hiển thị các sản phẩm liên quan đến từ khóa "Dress"

  @AE_SEARCH_004 @positive @high
  Scenario: AE_SEARCH_004 - Tìm kiếm sản phẩm Jeans bằng từ khóa chung
    When người dùng nhập từ khóa "Jeans" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And danh sách kết quả phải hiển thị các sản phẩm liên quan đến từ khóa "Jeans"
    And kết quả có thể bao gồm "Soft Stretch Jeans" hoặc "Regular Fit Straight Jeans"

  @AE_SEARCH_005 @positive @medium
  Scenario: AE_SEARCH_005 - Tìm kiếm bằng tên sản phẩm dài và cụ thể
    When người dùng nhập từ khóa "Pure Cotton V-Neck T-Shirt" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And sản phẩm phù hợp với tên "Pure Cotton V-Neck T-Shirt" phải xuất hiện trong kết quả tìm kiếm

  @AE_SEARCH_006 @negative @high
  Scenario: AE_SEARCH_006 - Tìm kiếm bằng từ khóa không tồn tại
    When người dùng nhập từ khóa "XYZNOTEXIST123" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then website không được phát sinh lỗi hệ thống
    And không được hiển thị sản phẩm không liên quan đến từ khóa "XYZNOTEXIST123"
    And người dùng vẫn ở trang tìm kiếm sản phẩm

  @AE_SEARCH_007 @negative @high @requirement_gap
  Scenario: AE_SEARCH_007 - Tìm kiếm khi không nhập từ khóa
    When người dùng để trống ô "Search Product"
    And người dùng nhấn nút "Search"
    Then website không được crash hoặc phát sinh lỗi hệ thống
    And trang Products vẫn phải sử dụng được

    # Requirement hiện tại chưa đặc tả danh sách kết quả
    # mong đợi khi người dùng search rỗng.

  @AE_SEARCH_008 @edge @medium @requirement_gap
  Scenario: AE_SEARCH_008 - Tìm kiếm với chỉ khoảng trắng
    When người dùng nhập "   " vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then website không được crash hoặc phát sinh lỗi hệ thống
    And trang Products vẫn phải hoạt động bình thường

    # Requirement hiện tại chưa xác định hệ thống
    # có trim khoảng trắng hay không.

  @AE_SEARCH_009 @edge @medium @requirement_gap
  Scenario: AE_SEARCH_009 - Tìm kiếm với chữ thường khác cách viết tên sản phẩm
    When người dùng nhập từ khóa "blue top" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And website không được phát sinh lỗi hệ thống

    # Cần xác định requirement tìm kiếm có phân biệt
    # chữ hoa và chữ thường hay không.

  @AE_SEARCH_010 @edge @medium @requirement_gap
  Scenario: AE_SEARCH_010 - Tìm kiếm bằng từ khóa viết toàn bộ chữ hoa
    When người dùng nhập từ khóa "TOP" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then tiêu đề "SEARCHED PRODUCTS" phải được hiển thị
    And website không được phát sinh lỗi hệ thống

    # Cần xác định requirement tìm kiếm có phân biệt
    # chữ hoa và chữ thường hay không.

  @AE_SEARCH_011 @negative @medium
  Scenario: AE_SEARCH_011 - Tìm kiếm với ký tự đặc biệt không tương ứng sản phẩm
    When người dùng nhập từ khóa "@#$%^&*" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then website không được crash hoặc phát sinh lỗi hệ thống
    And không được hiển thị sản phẩm không liên quan đến từ khóa đã nhập
    And trang Products vẫn phải hoạt động bình thường

  @AE_SEARCH_012 @negative @low
  Scenario: AE_SEARCH_012 - Tìm kiếm với chuỗi số không tồn tại trong tên sản phẩm
    When người dùng nhập từ khóa "123456789" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then website không được phát sinh lỗi hệ thống
    And không được hiển thị sản phẩm không liên quan đến từ khóa "123456789"
    And người dùng vẫn ở trang tìm kiếm sản phẩm

  @AE_SEARCH_013 @edge @medium
  Scenario: AE_SEARCH_013 - Tìm kiếm với từ khóa rất dài
    When người dùng nhập chuỗi gồm 256 ký tự "A" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then giao diện không được bị vỡ
    And website không được crash hoặc phát sinh lỗi hệ thống
    And hệ thống vẫn phải phản hồi bình thường

  @AE_SEARCH_014 @positive @high
  Scenario: AE_SEARCH_014 - Thực hiện hai lần tìm kiếm liên tiếp
    When người dùng nhập từ khóa "Top" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then danh sách kết quả phải hiển thị các sản phẩm liên quan đến từ khóa "Top"

    When người dùng thay từ khóa tìm kiếm bằng "Jeans"
    And người dùng nhấn nút "Search" lần nữa
    Then danh sách kết quả phải được cập nhật theo từ khóa "Jeans"
    And kết quả của lần tìm kiếm trước không được làm sai danh sách kết quả mới

  @AE_SEARCH_015 @positive @medium
  Scenario: AE_SEARCH_015 - Mở chi tiết sản phẩm từ kết quả tìm kiếm
    When người dùng nhập từ khóa "Blue Top" vào ô "Search Product"
    And người dùng nhấn nút "Search"
    Then sản phẩm "Blue Top" phải xuất hiện trong kết quả tìm kiếm

    When người dùng chọn "View Product" của sản phẩm "Blue Top"
    Then trang "Product Details" của sản phẩm "Blue Top" phải được hiển thị