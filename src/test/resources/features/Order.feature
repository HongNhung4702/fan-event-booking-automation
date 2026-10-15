@ui @order @automationexercise @crossbrowser
Feature: Đặt hàng sản phẩm trên Automation Exercise
  Với tư cách là người dùng Automation Exercise
  Tôi muốn thêm sản phẩm vào giỏ hàng và thực hiện đặt hàng
  Để có thể mua sản phẩm thành công trên hệ thống

  Background:
    Given người dùng mở trang chủ Automation Exercise
    Then trang chủ Automation Exercise phải được hiển thị

  @positive @high
  Scenario: AE_ORDER_001 - Thêm một sản phẩm vào giỏ hàng
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_001" vào giỏ hàng
    And người dùng mở giỏ hàng
    Then kết quả giỏ hàng phải đúng với bộ dữ liệu "AE_ORDER_001"

  @positive @high
  Scenario: AE_ORDER_002 - Thêm nhiều sản phẩm vào giỏ hàng
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_002" vào giỏ hàng
    And người dùng mở giỏ hàng
    Then kết quả giỏ hàng phải đúng với bộ dữ liệu "AE_ORDER_002"

  @edge @medium
  Scenario: AE_ORDER_003 - Thêm sản phẩm với số lượng bằng 4
    When người dùng mở chi tiết sản phẩm bằng bộ dữ liệu "AE_ORDER_003"
    And người dùng chọn số lượng theo bộ dữ liệu "AE_ORDER_003"
    And người dùng thêm sản phẩm từ trang chi tiết vào giỏ hàng
    And người dùng mở giỏ hàng
    Then kết quả giỏ hàng phải đúng với bộ dữ liệu "AE_ORDER_003"

  @positive @medium
  Scenario: AE_ORDER_004 - Xóa sản phẩm khỏi giỏ hàng
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_004" vào giỏ hàng
    And người dùng mở giỏ hàng
    And người dùng xóa sản phẩm trong bộ dữ liệu "AE_ORDER_004"
    Then kết quả giỏ hàng phải đúng với bộ dữ liệu "AE_ORDER_004"

  @negative @high
  Scenario: AE_ORDER_005 - Checkout khi chưa đăng nhập
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_005" vào giỏ hàng
    And người dùng mở giỏ hàng
    And người dùng chọn Proceed To Checkout
    Then hệ thống phải yêu cầu đăng ký hoặc đăng nhập

  @positive @high
  Scenario: AE_ORDER_006 - Checkout sau khi đăng nhập
    Given người dùng đăng nhập Automation Exercise bằng bộ dữ liệu "AE_ORDER_006"
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_006" vào giỏ hàng
    And người dùng mở giỏ hàng
    And người dùng chọn Proceed To Checkout
    Then trang Checkout phải được hiển thị

  @positive @high
  Scenario: AE_ORDER_007 - Kiểm tra thông tin địa chỉ tại Checkout
    Given người dùng đăng nhập Automation Exercise bằng bộ dữ liệu "AE_ORDER_007"
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_007" vào giỏ hàng
    And người dùng mở giỏ hàng
    And người dùng chọn Proceed To Checkout
    Then người dùng kiểm tra các khu vực Checkout:
      | khu vực           |
      | Delivery Address  |
      | Billing Address   |
      | Review Your Order |

  @positive @high
  Scenario Outline: <testCaseId> - Đặt hàng thành công
    Given người dùng đăng nhập Automation Exercise bằng bộ dữ liệu "<testCaseId>"
    When người dùng thêm sản phẩm bằng bộ dữ liệu "<testCaseId>" vào giỏ hàng
    And người dùng mở giỏ hàng
    And người dùng chọn Proceed To Checkout
    And người dùng chọn Place Order
    And người dùng nhập thông tin thanh toán bằng bộ dữ liệu "<testCaseId>"
    And người dùng xác nhận thanh toán
    Then kết quả đặt hàng phải đúng với bộ dữ liệu "<testCaseId>"

    Examples:
      | testCaseId    |
      | AE_ORDER_008  |
      | AE_ORDER_009  |

  @positive @medium
  Scenario: AE_ORDER_010 - Tải Invoice sau khi đặt hàng thành công
    Given người dùng đăng nhập Automation Exercise bằng bộ dữ liệu "AE_ORDER_010"
    When người dùng thêm sản phẩm bằng bộ dữ liệu "AE_ORDER_010" vào giỏ hàng
    And người dùng mở giỏ hàng
    And người dùng chọn Proceed To Checkout
    And người dùng chọn Place Order
    And người dùng nhập thông tin thanh toán bằng bộ dữ liệu "AE_ORDER_010"
    And người dùng xác nhận thanh toán
    Then kết quả đặt hàng phải đúng với bộ dữ liệu "AE_ORDER_010"
    When người dùng tải Invoice
    Then file Invoice phải được tải xuống thành công