class LogReportController < ApplicationController
	skip_before_filter :verify_authenticity_token

	def report
		puts "FILE FINE LOG!"
		puts params.keys
#		ss = params[:log_report].to_json
#		f = File.open(Rails.root.to_s + "/db/log/#{params[:REPORT_ID]}.log", 'w')
#		f.puts ss
#		f.close
#		pp params[:log_report]
#		mongo_client = MongoClient.new
#		puts mongo_client.database_names
		render :json => {
			'result' => 'OK'
		}.to_json
	end
end
