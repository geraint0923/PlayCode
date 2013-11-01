# Be sure to restart your server when you modify this file.

# Your secret key is used for verifying the integrity of signed cookies.
# If you change this key, all old signed cookies will become invalid!

# Make sure the secret is at least 30 characters and all random,
# no regular words or you'll be exposed to dictionary attacks.
# You can use `rake secret` to generate a secure secret key.

# Make sure your secret_key_base is kept private
# if you're sharing your code publicly.
ReportServer::Application.config.secret_key_base = 'b2f0db43dd847ced8779b3ff962e239a2dc4e4807bd5b27bc1c4870a4382a27843c58e9717732082fa816b8139b178a0785f0aff6d9dc9e50cd009cf12281496'
