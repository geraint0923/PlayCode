class UpdateController < ApplicationController
	skip_before_filter :verify_authenticity_token

	def check
		puts "FINEIEINASDASDA"
		puts params[:app_name]
		render :json => {
			"version_name" => "first version",
			"version_code" => "1"
		}.to_json
	end
end
