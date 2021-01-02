# CurrencyConverter

Build a simple currency converter that meet the following criteria:-

## USER INTERFACE
- The app should contain two drop-down displays that displays a list of countries
(iso-codes and flags).
- The app should contain an input field to enter the amount of currency that should be
converted and also a text view to show the result.
- The app should contain a button to switch between source and target currencies.
- The app has a menu item to turn on/off currency change monitoring feature.
- You can use any design you want as long as it has the required functions

## FUNCTIONALITY
- The users should be able to select the source currency and the target currency and
enter the amount they want to convert.
- The app should display the result of the amount entered by the user after being
converted to target currency.
- The user should be able to fast switch between source and target currency using a
button
- The start monitoring menu item should monitor the currency exchange rate of the current
currency pair and every one hour and notify the user if the rate changes.
- The user can stop monitoring any time using the same menu item.
- The menu item icon should reflect if monitoring is running or not.

## ARCHITECTURE
- You should use MVVM architecture with Android Architecture Components such as
LiveData and ViewModel.


<img src="https://user-images.githubusercontent.com/41232970/103453550-f2d0f600-4ce3-11eb-835e-be5bddb5dd16.png" data-canonical-src="https://user-images.githubusercontent.com/41232970/103453550-f2d0f600-4ce3-11eb-835e-be5bddb5dd16.png" width="200" height="600" />

## API
- The app should be connected with a Web API to get the conversion rate or to convert
the amount directly so the result should be up-to-date not statically saved in the app.
- These API providers might be helpful but it's up to you to search and find better ones if
you want.
- https://fixer.io/
- https://currencylayer.com/
- https://currencyapi.net/
- https://exchangeratesapi.io/
- https://exchangeratesapi.io/
- https://docs.openexchangerates.org/docs/convert
- https://www.countryflags.io/

● You can use any HTTP client or library like Volley or Retrofit.
