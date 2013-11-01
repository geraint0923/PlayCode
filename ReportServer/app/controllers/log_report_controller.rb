include Mongo
require 'pp'
class LogReportController < ApplicationController
	skip_before_filter :verify_authenticity_token

	def traverse_hash(inmap, outmap)
		inmap.each do |key, value|
			if Hash === value
				om = Hash.new
				outmap[key.gsub(".", "_")] = om
				traverse_hash(value, om)
			else
				outmap[key.gsub(".", "_")] = value
			end
		end
	end

	def report
		puts "FILE FINE LOG!"
		puts params.keys
#		ss = params[:log_report].to_json
#		f = File.open(Rails.root.to_s + "/db/log/#{params[:REPORT_ID]}.log", 'w')
#		f.puts ss
#		f.close
#		pp params[:log_report]
		
		mongo_client = MongoClient.new
		puts mongo_client.database_names
		db = mongo_client.db("test")
		coll = db.collection("log")
		recs = Hash.new
		traverse_hash(params[:log_report], recs)
#		pp recs
		coll.insert(recs)
		render :json => {
			'result' => 'OK'
		}.to_json
	end
end
